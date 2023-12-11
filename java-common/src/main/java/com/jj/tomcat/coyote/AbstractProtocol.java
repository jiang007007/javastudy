package com.jj.tomcat.coyote;

import com.jj.tomcat.coyote.body.RequestGroupInfo;
import com.jj.tomcat.util.net.AbstractEndpoint;
import com.jj.tomcat.util.net.SocketEvent;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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

    protected abstract Processor createProcessor();

    protected AbstractEndpoint<?> getEndpoint() {
        return this.endpoint;
    }

    /**
     * 连接处理基类
     *
     * @param <S> NioChannel
     */
    protected static class ConnectionHandler<S> implements AbstractEndpoint.Handler<S> {
        private final AbstractProtocol<S> proto;

        private final RequestGroupInfo global = new RequestGroupInfo();
        //注册数
        private final AtomicLong registerCount = new AtomicLong(0);

        private final Map<S, Processor> connections = new ConcurrentHashMap<>();

        public ConnectionHandler(AbstractProtocol<S> proto) {
            this.proto = proto;
        }

        /**
         * 根据状态 处理连接
         *
         * @param wrapper NioSocketWrapper
         * @param states  Socket 连接状态
         * @return 返回处理后的socket状态
         */
        @Override
        public SocketState process(SocketWrapperBase<S> wrapper, SocketEvent states) {
            if (wrapper == null) {
                return SocketState.CLOSE;
            }
            //通过NioSocketWrapper包装类获取NioChannel
            S socket = wrapper.getSocket();
            //在缓存中获取Processor,处理协议类
            Processor processor = connections.get(socket);
            if (processor != null) {
                //移除等待队列中的Process
            } else if (states == SocketEvent.DISCONNECT || states == SocketEvent.ERROR) {
                //客户端断开连接,程序处理错误
                return SocketState.CLOSE;
            }
            return null;
        }

        @Override
        public void pause() {

        }

        @Override
        public void recycle() {

        }
    }


    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }
}
