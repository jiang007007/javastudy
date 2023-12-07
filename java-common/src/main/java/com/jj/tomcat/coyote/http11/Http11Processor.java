package com.jj.tomcat.coyote.http11;

import com.jj.tomcat.coyote.AbstractProcessor;
import com.jj.tomcat.coyote.ActionCode;
import com.jj.tomcat.util.net.AbstractEndpoint;
import com.jj.tomcat.util.net.SocketEvent;
import com.jj.tomcat.util.net.channel.NioChannel;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

import java.io.IOException;

public class Http11Processor extends AbstractProcessor{

    private final AbstractHttp11Protocol<?> protocol;


    public Http11Processor(AbstractHttp11Protocol<?> protocol,AbstractEndpoint<?> endpoint) {
        super(endpoint);
        this.protocol = protocol;
    }

    @Override
    protected AbstractEndpoint.Handler.SocketState service(SocketWrapperBase<? extends NioChannel> socketWrapper) throws IOException {
        return null;
    }

    @Override
    protected AbstractEndpoint.Handler.SocketState dispatch(SocketEvent status) throws IOException {
        return null;
    }

    @Override
    public void action(ActionCode actionCode, Object param) {

    }
}
