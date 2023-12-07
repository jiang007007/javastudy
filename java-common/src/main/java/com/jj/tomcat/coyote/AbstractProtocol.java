package com.jj.tomcat.coyote;

import com.jj.tomcat.util.net.AbstractEndpoint;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

public abstract class AbstractProtocol<S> implements ProtocolHandler {

    //提供低级别的网络IO
    private final AbstractEndpoint<S> endpoint;

    //对NioChannel处理接口
    private AbstractEndpoint.Handler<S> handler;

    //协议处理器
    private Adapter adapter;

    public AbstractProtocol(AbstractEndpoint<S> endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * 连接处理基类
     * @param <S> NioChannel
     */
    protected static class ConnectionHandler<S> implements AbstractEndpoint.Handler<S> {
        @Override
        public SocketState process(SocketWrapperBase<S> socket, SocketState states) {
            return null;
        }

        @Override
        public void pause() {

        }

        @Override
        public void recycle() {

        }
    }
}
