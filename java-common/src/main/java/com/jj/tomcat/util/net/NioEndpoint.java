package com.jj.tomcat.util.net;

import com.jj.tomcat.util.collections.SynchronizedQueue;
import com.jj.tomcat.util.collections.SynchronizedStack;
import com.jj.tomcat.util.net.buffer.SocketBufferHandler;
import com.jj.tomcat.util.net.channel.NioChannel;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NioEndpoint extends AbstractEndpoint<NioChannel> {

    public static final int OP_REGISTER = 0x100; //register interest op

    private SynchronizedStack<NioChannel> nioChannels;

    /**
     * Cache for poller events
     */
    private SynchronizedStack<PollerEvent> eventCache;

    private long selectorTimeout = 1000;


    /**
     * Server socket "pointer".
     */
    private volatile ServerSocketChannel serverSock = null;

    private Poller[] pollers = null;
    private AtomicInteger pollerRotater = new AtomicInteger(0);

    public Poller getPoller0() {
        int idx = Math.abs(pollerRotater.incrementAndGet()) % pollers.length;
        return pollers[idx];
    }

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
            //感兴趣的事件设置为0,不会响应事件
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
                            //当前事件和注册好的事件取并集
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

        private Poller poller = null;
        private int interestOps = 0;
        private volatile boolean closed = false;

        private volatile SendfileData sendfileData = null;

        public NioSocketWrapper(NioChannel socket, NioEndpoint endpoint) {
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

        public Poller getPoller() {
            return poller;
        }

        public void setPoller(Poller poller) {
            this.poller = poller;
        }

        public void setSendfileData(SendfileData sf) {
            this.sendfileData = sf;
        }

        public SendfileData getSendfileData() {
            return this.sendfileData;
        }
    }

    @Override
    public AbstractEndpoint.Acceptor createAcceptor() {
        return new Acceptor();
    }

    //具体子类创建SocketProcess
    @Override
    protected SocketProcessorBase<NioChannel> createSocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent event) {
        return new SocketProcessor(socketWrapper, event);
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
                SocketChannel socket;
                try {
                    try {
                        socket = serverSock.accept();//获取准备好的连接事件
                    } catch (IOException ioe) {
                        throw ioe;
                    }

                    if (running && !paused) {
                        if (!setSocketOptions(socket)) {
                            //向poller添加事件不成功关闭Socket
                        }
                    }
                } catch (Throwable e) {
                    //外层catch不抛出异常只记录日志
                }
            }
            state = AcceptorState.ENDED;
        }
    }

    /**
     * 封装SocketChannel和SocketBufferHandler 为NioChannel
     *
     * @param socket
     * @return
     */
    protected boolean setSocketOptions(SocketChannel socket) {
        //处理连接
        try {
            socket.configureBlocking(false);
            //获取客户端的Socket
            Socket sock = socket.socket();
            //设置客户端的socket属性
            socketProperties.setProperties(sock);
            //取出缓冲中的NioChannel对象
            NioChannel channel = nioChannels.pop();
            if (channel == null) {
                SocketBufferHandler bufferHandler = new SocketBufferHandler(
                        socketProperties.getAppReadBufSize(),
                        socketProperties.getAppWriteBufSize(),
                        socketProperties.getDirectBuffer());
                channel = new NioChannel(socket, bufferHandler);
            } else {
                //存在对象(对象池)
                channel.setIOChannel(socket);
                //重置缓冲器
                channel.reset();
            }
            getPoller0().register(channel);
        } catch (Throwable t) {
            return false;
        }
        return true;
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

        //提供添加事件到队列中
        private void addEvent(PollerEvent event) {
            events.offer(event);//头添加  尾部取
            if (wakeupCounter.incrementAndGet() == 0) {
                selector.wakeup();
            }
        }

        /**
         * 轮询Poller中event队列
         */
        public boolean events() {
            boolean result = false;
            PollerEvent pe;
            for (int i = 0, size = events.size(); i < size && (pe = events.poll()) != null; i++) {
                result = true;
                try {
                    pe.run();
                    pe.reset();//清空PollerEvent的NioChannel,NioSocketWrapper,注册事件为0
                    if (running && !paused) {
                        eventCache.push(pe);
                    }
                } catch (Throwable x) {

                }
            }

            return result;
        }

        /**
         * 向poller中注册新创建的socket
         *
         * @param socket
         */
        public void register(final NioChannel socket) {
            socket.setPoller(this);
            //NioChannel的包装类
            NioSocketWrapper ka = new NioSocketWrapper(socket, NioEndpoint.this);

            socket.setSocketWrapper(ka);

            //设置NioSocketWrapper属性
            ka.setPoller(this);
            ka.setReadTimeout(getSocketProperties().getSoTimeout());
            ka.setWriteTimeout(getSocketProperties().getSoTimeout());
            ka.setKeepAliveLeft(NioEndpoint.this.getMaxKeepAliveRequests());

            PollerEvent r = eventCache.pop();

            ka.interestOps(SelectionKey.OP_READ);
            if (r == null) {
                r = new PollerEvent(socket, ka, OP_REGISTER);
            } else {
                r.reset(socket, ka, OP_REGISTER);
            }
            //把PollerEvent 添加到Poller的队列
            addEvent(r);
        }


        @Override
        public void run() {
            while (true) {
                boolean hasEvents = false;
                try {
                    if (!close) {
                        //注册
                        hasEvents = events();
                        //获取selector准备好的事件,队列存在PollerEvent 立即返回准备好的事件,队列没有PollerEvent 则超时等待
                        if (wakeupCounter.getAndSet(-1) > 0) {
                            keyCount = selector.selectNow();
                        } else {
                            keyCount = selector.select(selectorTimeout);
                        }
                        wakeupCounter.set(0);
                    }
                } catch (Throwable t) {
                    continue;
                }
                if (keyCount == 0) {
                    hasEvents = (hasEvents | events());
                }
                Iterator<SelectionKey> iterator = keyCount > 0 ? selector.selectedKeys().iterator() : null;
                while (iterator != null && iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    NioSocketWrapper attachment = (NioSocketWrapper) sk.attachment();
                    iterator.remove();
                    if (attachment != null) {
                        //处理
                        processKey(sk, attachment);
                    }
                }
            }
        }

        //处理准备好的事件
        protected void processKey(SelectionKey sk, NioSocketWrapper attachment) {
            try {
                if (close) {
                    //poller 关闭状态关闭 该key添加到取消key的集合中(cancelledKey),关闭socket,SocketChannel

                } else if (sk.isValid() && attachment != null) {
                    if (sk.isReadable() || sk.isWritable()) {
                        if (sk.isValid() && attachment.getSendfileData() != null) {
                            //sendfile处理读写事件
                        } else {
                            //取消注册 selectkey 感兴趣的事件设置为0 即不响应该事件
                            //sk.readyOps() 获取就绪项
                            unreg(sk, attachment, sk.readyOps());
                            boolean closeSocket = false;
                            //处理读事件
                            if (sk.isReadable() && !processSocket(attachment, SocketEvent.OPEN_READ, true)) {
                                closeSocket = true;
                            }
                            if (!closeSocket && sk.isWritable() && !processSocket(attachment, SocketEvent.OPEN_WRITE, true)) {
                                closeSocket = true;
                            }
                            //如果处理Socket连接失败则关闭sockChannel
                            if (closeSocket) {

                            }
                        }
                    }
                } else {

                }
            } catch (CancelledKeyException ckx) {

            } catch (Throwable t) {
                //如果不是取消监听key异常
            }
        }

        protected void unreg(SelectionKey sk, NioSocketWrapper attachment, int readyOps) {
            reg(sk, attachment, sk.interestOps() & (~readyOps));
        }

        protected void reg(SelectionKey sk, NioSocketWrapper attachment, int readyOps) {
            sk.interestOps(readyOps);
            attachment.interestOps(readyOps);
        }

        public NioSocketWrapper cancelledKey(SelectionKey key) {
            NioSocketWrapper ka = null;
            if (key == null) {
                return null;
            }
            try {
                //把Selectionkey中维护的SocketWrapper 对象设置为空
                ka = (NioSocketWrapper) key.attach(null);

            } catch (Throwable t) {

            }
            return ka;
        }
    }


    public static class SendfileData extends SendfileDataBase {
        public SendfileData(String fileName, long pos, long length) {
            super(fileName, pos, length);
        }

        protected volatile FileChannel fchannel;
    }


    //执行线程池的任务
    protected class SocketProcessor extends SocketProcessorBase<NioChannel> {


        public SocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent event) {
            super(socketWrapper, event);
        }

        @Override
        protected void doRun() {
            NioChannel socket = socketWrapper.getSocket();
            SelectionKey key = socket.getIOChannel().keyFor(socket.getPoller().getSelector());
            try {
                int handshake = -1;
                try {

                    //默认处理握手已完成的连接
                    if (socket.isHandshakeComplete()) {
                        handshake = 0;
                    } else if (event == SocketEvent.STOP || event == SocketEvent.DISCONNECT ||
                            event == SocketEvent.ERROR) {
                        //SSL 握手期间出错 IOException
                        handshake = -1;
                    } else {
                        handshake = socket.handshake(key.isReadable(), key.isWritable());
                        event = SocketEvent.OPEN_READ;
                    }
                } catch (IOException e) {
                    handshake = -1;
                } catch (CancelledKeyException ckx) {
                    handshake = -1;
                }
                //处理请求的Socket(默认使用Http11Protocol->ConnectionHandler)
                if (handshake == 0) {

                }
            } catch (CancelledKeyException e) {

            } finally {
                socketWrapper = null;
                event = null;
                if (running && !paused) {
                    processorCache.push(this);
                }
            }
        }
    }

    //NioChannel对象使用完成后,取消对应的key 归还到对象池中
    private void close(NioChannel socket, SelectionKey key) {
        //不等于空 则说明未归还
        if (socket.getPoller().cancelledKey(key) != null) {
            if (running && !paused) {
                nioChannels.push(socket);
            }
        }
    }

    public static void main(String[] args) {
        AtomicInteger wa = new AtomicInteger(1);
        System.out.println(wa.getAndSet(-1));
        System.out.println(4 & ~4);
        NioEndpoint nioEndpoint = new NioEndpoint();
        nioEndpoint.running = true;
        AbstractEndpoint.Acceptor acceptor = nioEndpoint.createAcceptor();
        Thread thread = new Thread(acceptor);
        thread.start();
    }
}
