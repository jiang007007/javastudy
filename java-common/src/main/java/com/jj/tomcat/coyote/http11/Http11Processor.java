package com.jj.tomcat.coyote.http11;

import com.jj.tomcat.coyote.AbstractProcessor;
import com.jj.tomcat.coyote.ActionCode;
import com.jj.tomcat.coyote.body.RequestInfo;
import com.jj.tomcat.util.net.AbstractEndpoint;
import com.jj.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
import com.jj.tomcat.util.net.SocketEvent;
import com.jj.tomcat.util.net.channel.NioChannel;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

import java.io.IOException;

/**
 * 请求处理器
 */
public class Http11Processor extends AbstractProcessor {

    private final AbstractHttp11Protocol<?> protocol;


    public Http11Processor(AbstractHttp11Protocol<?> protocol, AbstractEndpoint<?> endpoint) {
        super(endpoint);
        this.protocol = protocol;
    }

    @Override
    protected SocketState service(SocketWrapperBase<?> socketWrapper) throws IOException {
        RequestInfo rp = request.getRequestProcessor();

        //
        setSocketWrapper(socketWrapper);
        try {
            //调用CoyoteAdapter service方法 进行请求体的解析  pipeline回调  结果刷新出去
            getAdapter().service(request, response);
        } catch (Throwable e) {

        }
        return null;
    }

    @Override
    protected SocketState dispatch(SocketEvent status) throws IOException {
        return null;
    }

    @Override
    public void action(ActionCode actionCode, Object param) {

    }

    @Override
    protected final void setSocketWrapper(SocketWrapperBase<?> socketWrapper) {
        super.setSocketWrapper(socketWrapper);
    }

}
