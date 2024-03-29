package com.jj.tomcat.util.net;

import com.jj.tomcat.util.collections.SynchronizedStack;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

import java.core.TaskQueue;
import java.core.TaskThreadFactory;
import java.core.TomcatThreadPoolExecutor;
import java.net.InetAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 摘取Tomcat的NIO模块.舍去SSL,JMX模块,
 *
 * @param <S> 被EndPoint管理的sockets
 */
public abstract class AbstractEndpoint<S> {


    /**
     * Running state of the endpoint.
     */
    protected volatile boolean running = false;


    /**
     * Will be set to true whenever the endpoint is paused.
     */
    protected volatile boolean paused = false;

    /**
     * Are we using an internal executor
     */
    protected volatile boolean internalExecutor = true;

    //SocketProperties: 设置Socket属性:包括 发送缓冲区 接收缓冲区,保活时间等
    protected SocketProperties socketProperties = new SocketProperties();


    protected SynchronizedStack<SocketProcessorBase<S>> processorCache;

    /**
     * Has the user requested that send file be used where possible?
     */
    private boolean useSendfile = true;

    /**
     * Acceptor thread count.
     */
    protected int acceptorThreadCount = 1;

    /**
     * External Executor based thread pool.
     */
    private Executor executor = null;
    /**
     * Server socket port.
     */
    private int port;

    /**
     * Address for the server socket.
     */
    private InetAddress address;

    /**
     * 控制Endpoint 绑定端口
     * true: 在调用init方法绑定端口  destroy方法 解绑端口
     * false: 在调用start方法绑定口,stop 方法 解绑端口
     */
    private boolean bindOnInit = true;

    /**
     * Max keep alive requests
     */
    private int maxKeepAliveRequests = 100; // as in Apache HTTPD server

    private volatile BindState bindState = BindState.UNBOUND;

    /**
     * Maximum amount of worker threads.
     */
    private int maxThreads = 200;


    public int getMaxThreads() {
        if (internalExecutor) {
            return maxThreads;
        } else {
            return -1;
        }
    }


    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
        this.internalExecutor = (executor == null);
    }

    public Executor getExecutor() {
        return executor;
    }


    public void setAcceptorThreadCount(int acceptorThreadCount) {
        this.acceptorThreadCount = acceptorThreadCount;
    }

    public int getAcceptorThreadCount() {
        return acceptorThreadCount;
    }


    public boolean getUseSendfile() {
        return useSendfile;
    }

    public void setUseSendfile(boolean useSendfile) {
        this.useSendfile = useSendfile;
    }

    public SocketProperties getSocketProperties() {
        return socketProperties;
    }

    public int getMaxKeepAliveRequests() {
        return maxKeepAliveRequests;
    }

    public void setMaxKeepAliveRequests(int maxKeepAliveRequests) {
        this.maxKeepAliveRequests = maxKeepAliveRequests;
    }


    public void createExecutor() {
        internalExecutor = true;
        TaskQueue taskQueue = new TaskQueue();
        TaskThreadFactory tf = new TaskThreadFactory("TP-exec", true, Thread.NORM_PRIORITY);
        executor = new TomcatThreadPoolExecutor(10, 200, 60, TimeUnit.SECONDS, taskQueue, tf);
        taskQueue.setParent((TomcatThreadPoolExecutor) executor);
    }


    /**
     * net模块状态:绑定状态 绑定并初始化 绑定并开始 关闭socket并停止该模块
     */
    protected enum BindState {
        UNBOUND, BOUND_ON_INIT, BOUND_ON_START, SOCKET_CLOSED_ON_STOP
    }

    //对NioChannel的处理接口
    public interface Handler<S> {
        enum SocketState {
            OPEN, CLOSED, LONG, ASYNC_END, SENDFILE, UPGRADING, SUSPENDED
        }
        //根据Socket状态处理指定的socket ->socketChannel
        SocketState process(SocketWrapperBase<S> socket,SocketEvent states);

        void pause();

        void recycle();
    }

    //获取客户端准备好的连接 抽象类 抽取出线程的维护状态,和线程名
    public abstract static class Acceptor implements Runnable {
        public enum AcceptorState {
            NEW, RUNNING, PAUSED, ENDED
        }

        //初始化状态
        protected volatile AcceptorState state = AcceptorState.NEW;

        private String threadName;

        protected final void setThreadName(final String threadName) {
            this.threadName = threadName;
        }

        protected final String getThreadName() {
            return threadName;
        }
    }

    //钩子方法具体子类创建Acceptor的行为
    public abstract Acceptor createAcceptor();

    protected abstract SocketProcessorBase<S> createSocketProcessor(SocketWrapperBase<S> socketWrapper, SocketEvent event);


    //模板算法 统一处理worker线程的连接事件
    public boolean processSocket(SocketWrapperBase<S> attachment, SocketEvent event, boolean dispatch) {
        try {
            if (attachment == null) {
                return false;
            }
            SocketProcessorBase<S> processorBase = processorCache.pop();
            if (processorBase == null) {
                processorBase = createSocketProcessor(attachment, event);
            } else {
                processorBase.reset(attachment, event);
            }
            Executor executor = getExecutor();//获取线程池
            if (dispatch && executor != null) {
                executor.execute(processorBase);
            } else {
                processorBase.run();
            }
        } catch (RejectedExecutionException ree) {
            //线程池已满
            return false;

        } catch (Throwable t) {
            // This means we got an OOM or similar creating a thread, or that
            // the pool and its queue are full
        }
        return true;
    }
}
