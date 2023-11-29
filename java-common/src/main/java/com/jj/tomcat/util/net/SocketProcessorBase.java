package com.jj.tomcat.util.net;

import com.jj.tomcat.util.net.channel.SocketWrapperBase;

import java.util.Objects;

/**
 * 处理包装好的SocketWrapperBase
 */
public abstract class SocketProcessorBase<S> implements Runnable {
    protected SocketWrapperBase<S> socketWrapper;
    protected SocketEvent event;

    public SocketProcessorBase(SocketWrapperBase<S> socketWrapper, SocketEvent event) {
        reset(socketWrapper, event);
    }


    public void reset(SocketWrapperBase<S> socketWrapper, SocketEvent event) {
        Objects.requireNonNull(event);
        this.socketWrapper = socketWrapper;
        this.event = event;
    }


    @Override
    public void run() {
        synchronized (socketWrapper) {
            if (socketWrapper.isClosed()) {
                return;
            }
            doRun();
        }
    }

    protected abstract void doRun();
}
