package com.jj.tomcat.util.net.channel;

import com.jj.tomcat.util.net.AbstractEndpoint;
import com.jj.tomcat.util.net.buffer.SocketBufferHandler;

public abstract class SocketWrapperBase<E> {

    private final E socket;//NioChannel(对ServerSocket的包装)

    private final AbstractEndpoint<E> endpoint;

    protected volatile SocketBufferHandler socketBufferHandler;


    public SocketWrapperBase(E socket, AbstractEndpoint<E> endpoint) {
        this.socket = socket;
        this.endpoint = endpoint;
    }

    public abstract boolean isClosed();
}
