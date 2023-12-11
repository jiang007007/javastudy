package com.jj.tomcat.coyote.http11;

import com.jj.tomcat.coyote.AbstractProtocol;
import com.jj.tomcat.coyote.Processor;
import com.jj.tomcat.util.net.AbstractEndpoint;

public abstract class AbstractHttp11Protocol<S> extends AbstractProtocol<S> {

    public AbstractHttp11Protocol(AbstractEndpoint<S> endpoint) {
        super(endpoint);
        //创建连接处理
    }


    //创建协议实例  处理客户端连接与servlet 关联
    @Override
    protected Processor createProcessor() {
        Http11Processor processor = new Http11Processor(this, getEndpoint());
        //组合请求处理器
        //1  客户端请求交给协议处理器  解析http请求,组装成Request  Response
        //2  结果交个CoyoteAdapter 处理 初始化servlet 解析web.xml 调用自定义Servlet
        processor.setAdapter(getAdapter());
        return processor;
    }

    public AbstractEndpoint<?> getEndpoint() {
        return super.getEndpoint();
    }
}
