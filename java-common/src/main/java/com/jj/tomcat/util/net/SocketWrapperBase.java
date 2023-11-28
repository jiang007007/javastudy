package com.jj.tomcat.util.net;

public abstract class SocketWrapperBase<E> {

    private final E socket;//NioSocketWrapper

    private final AbstractEndpoint<E> endpoint;


    public SocketWrapperBase(E socket, AbstractEndpoint<E> endpoint) {
        this.socket = socket;
        this.endpoint = endpoint;
    }

    public abstract boolean isClosed();
}
