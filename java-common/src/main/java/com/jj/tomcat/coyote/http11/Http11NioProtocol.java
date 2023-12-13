package com.jj.tomcat.coyote.http11;

import com.jj.tomcat.coyote.AbstractProtocol;
import com.jj.tomcat.util.net.NioEndpoint;
import com.jj.tomcat.util.net.channel.NioChannel;

import java.util.concurrent.Executor;

/**
 * 协议实现类
 * 基于特定流协议的单线程处理器
 */
public class Http11NioProtocol extends AbstractHttp11Protocol<NioChannel> {

    public Http11NioProtocol() {
        super(new NioEndpoint());//创建Nio端点类
    }

    @Override
    public Executor getExecutor() {
        return null;
    }
}
