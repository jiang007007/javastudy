package com.jj.tomcat.util.net;

import com.jj.tomcat.util.collections.SynchronizedStack;
import com.jj.tomcat.util.net.channel.NioChannel;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioEndpoint extends AbstractEndpoint<NioChannel> {

    public static final int OP_REGISTER = 0x100; //register interest op

    private SynchronizedStack<NioChannel> nioChannels;


    /**
     * Server socket "pointer".
     */
    private volatile ServerSocketChannel serverSock = null;


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
