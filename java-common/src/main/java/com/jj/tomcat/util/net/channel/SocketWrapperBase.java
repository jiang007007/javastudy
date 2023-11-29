package com.jj.tomcat.util.net.channel;

import com.jj.tomcat.util.net.AbstractEndpoint;

public abstract class SocketWrapperBase<E> {

    private final E socket;//NioChannel(对ServerSocket的包装)

    private final AbstractEndpoint<E> endpoint;


    public SocketWrapperBase(E socket, AbstractEndpoint<E> endpoint) {
        this.socket = socket;
        this.endpoint = endpoint;
    }

    public abstract boolean isClosed();
}
