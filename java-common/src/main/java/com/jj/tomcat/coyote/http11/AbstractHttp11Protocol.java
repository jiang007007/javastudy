package com.jj.tomcat.coyote.http11;

import com.jj.tomcat.coyote.AbstractProtocol;
import com.jj.tomcat.util.net.AbstractEndpoint;

public abstract class AbstractHttp11Protocol <S> extends AbstractProtocol<S> {

    public AbstractHttp11Protocol(AbstractEndpoint<S> endpoint){
        super(endpoint);
        //创建连接处理
    }

}
