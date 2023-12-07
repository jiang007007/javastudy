package com.jj.tomcat.coyote;

import com.jj.tomcat.util.net.AbstractEndpoint;
import com.jj.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
import com.jj.tomcat.util.net.DispatchType;
import com.jj.tomcat.util.net.SocketEvent;
import com.jj.tomcat.util.net.channel.NioChannel;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

import java.io.IOException;
import java.util.Iterator;

/**
 * 轻量级的协议处理器实现
 */
public abstract class AbstractProcessorLight implements Processor {

    @Override
    public SocketState process(SocketWrapperBase<? extends NioChannel> socketWrapper, SocketEvent status) throws IOException {
        SocketState state = SocketState.CLOSE;//状态机的实现
        Iterator<DispatchType> dispatches = null;
        do {
            if (dispatches != null) {
                //获取分发类型处理
                DispatchType nextDispatch = dispatches.next();
                //根据类型做读写处理
                state = dispatch(nextDispatch.getSocketStatus());
            } else if (status == SocketEvent.OPEN_READ) {
                state = service(socketWrapper);
            }

        } while (state == SocketState.ASYNC_END ||
                state != null && state != SocketState.CLOSE);
        return state;
    }

    /**
     * 处理标准的HTTP请求
     *
     * @param socketWrapper
     * @return
     * @throws IOException
     */
    protected abstract SocketState service(SocketWrapperBase<? extends NioChannel> socketWrapper) throws IOException;

    protected abstract SocketState dispatch(SocketEvent status) throws IOException;
}
