package com.jj.tomcat.util.net;

import com.jj.tomcat.util.collections.SynchronizedQueue;
import com.jj.tomcat.util.collections.SynchronizedStack;
import com.jj.tomcat.util.net.channel.NioChannel;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

import java.io.IOException;
import java.nio.channels.*;
import java.util.concurrent.atomic.AtomicLong;

public class NioEndpoint extends AbstractEndpoint<NioChannel> {

    public static final int OP_REGISTER = 0x100; //register interest op

    private SynchronizedStack<NioChannel> nioChannels;


    /**
     * Server socket "pointer".
     */
    private volatile ServerSocketChannel serverSock = null;

    /**
     * 1.把channel注册到select中
     * 2. 重置注册事件
     */
    public static class PollerEvent implements Runnable {
        private NioChannel socket;
        private NioSocketWrapper socketWrapper;
        private int interestOps;

        public PollerEvent(NioChannel socket, NioSocketWrapper socketWrapper, int interestOps) {
            reset(socket, socketWrapper, interestOps);
        }

        public void reset(NioChannel socket, NioSocketWrapper socketWrapper, int interestOps) {
            this.socket = socket;
            this.socketWrapper = socketWrapper;
            this.interestOps = interestOps;
        }

        //重置: NioChannel,NioSocketWrapper 置null,事件为0
        public void reset() {
            reset(null, null, 0);
        }

        @Override
        public void run() {
            //初始化状态 则对selector 注册为读事件
            if (this.interestOps == OP_REGISTER) {
                //att 注册回调函数(对象)
                try {
                    socket.getIOChannel().register(socket.getPoller().selector, SelectionKey.OP_READ, socketWrapper);
                } catch (ClosedChannelException e) {
                    throw new RuntimeException(e);
                }
            } else {
                //获取SelectKey
                SelectionKey selectionKey = socket.getIOChannel().keyFor(socket.getPoller().selector);
                try {
                    if (selectionKey == null) {
                        //未找到selector对应的key 设置NioSocketWrapper不可用
                        ((NioSocketWrapper) socket.getSocketWrapper()).closed = true;
                    } else {
                        NioSocketWrapper socketWrapper = (NioSocketWrapper) selectionKey.attachment();
                        if (socketWrapper != null) {
                            int ops = selectionKey.interestOps() | interestOps;//二进制组合  有1则1
                            socketWrapper.interestOps(ops);
                            selectionKey.interestOps(ops);//对应channel更新注册事件
                        }
                    }
                } catch (CancelledKeyException ckx) {

                }
            }
        }
    }


    //对NioEndPoint 和NioChannel封装
    public static class NioSocketWrapper extends SocketWrapperBase<NioChannel> {

        private int interestOps = 0;
        private volatile boolean closed = false;

        public NioSocketWrapper(NioChannel socket, AbstractEndpoint<NioChannel> endpoint) {
            super(socket, endpoint);
            socketBufferHandler = socket.getBufHandler();
        }

        public int interestOps() {
            return interestOps;
        }

        public int interestOps(int ops) {
            this.interestOps = ops;
            return ops;
        }

        @Override
        public boolean isClosed() {
            return closed;
        }
    }

    @Override
    public AbstractEndpoint.Acceptor createAcceptor() {
        return new Acceptor();
    }

    //非静态内部类 依赖外部类实例存在
    protected class Acceptor extends AbstractEndpoint.Acceptor {

        @Override
        public void run() {
            int errorDelay = 0;
            //默认为false
            while (running) {
                //挂起并且还在运行 ->准备关闭接收ServerSocketChannel的线程
                while (paused && running) {
                    state = AcceptorState.PAUSED;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {

                    }
                }
                //
                if (!running) {
                    break;
                }
                //真正处理连接逻辑
                state = AcceptorState.RUNNING;
                SocketChannel socket = null;
                try {
                    try {
                        socket = serverSock.accept();//获取准备好的连接事件
                    } catch (IOException ioe) {
                        throw ioe;
                    }

                    if (running && !paused) {

                    }
                } catch (Throwable e) {
                    //外层catch不抛出异常只记录日志
                }
            }
        }
    }

    public class Poller implements Runnable {
        private Selector selector;
        //缓冲队列 缓冲Acceptor提交的PollerEvent
        private final SynchronizedQueue<PollerEvent> events = new SynchronizedQueue<>();

        private volatile boolean close = false;

        private AtomicLong wakeupCounter = new AtomicLong(0);

        //selector准备好的事件数
        private volatile int keyCount = 0;

        public Poller() throws IOException {
            selector = Selector.open();//打开多路复用器
        }

        public int getKeyCount() {
            return keyCount;
        }

        public Selector getSelector() {
            return selector;
        }

        @Override
        public void run() {

        }

        //提供添加事件到队列中
        private void addEvent(PollerEvent event) {
            events.offer(event);//头添加  尾部取
            if (wakeupCounter.incrementAndGet() == 0){
                selector.wakeup();
            }
        }
    }

    private void close(NioChannel socket, SelectionKey key) {
        nioChannels.push(socket);
    }

    public static void main(String[] args) {
        NioEndpoint nioEndpoint = new NioEndpoint();
        nioEndpoint.running = true;
        AbstractEndpoint.Acceptor acceptor = nioEndpoint.createAcceptor();
        Thread thread = new Thread(acceptor);
        thread.start();
    }
}
