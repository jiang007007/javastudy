package com.jj.tomcat.coyote;

import com.jj.tomcat.coyote.body.Request;
import com.jj.tomcat.coyote.body.Response;
import com.jj.tomcat.util.net.AbstractEndpoint;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

/**
 * AbstractProcessorLight 连接处理器方法 processor 根据SocketEvent状态进行处理
 * 封装了endpoint Request  Response
 */
public abstract class AbstractProcessor extends AbstractProcessorLight implements ActionHook {

    private Adapter adapter;

    protected final AbstractEndpoint<?> endpoint;
    protected final Request request;
    protected final Response response;

    protected volatile SocketWrapperBase<?> socketWrapper = null;

    public AbstractProcessor(AbstractEndpoint<?> endpoint) {
        this(endpoint, new Request(), new Response());
    }

    public AbstractProcessor(AbstractEndpoint<?> endpoint, Request request, Response response) {
        this.endpoint = endpoint;
        this.request = request;
        this.response = response;

        this.request.setResponse(this.response);
        this.request.setHook(this);

        this.response.setHook(this);

    }
}
