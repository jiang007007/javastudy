package com.jj.tomcat.connector;

import com.jj.tomcat.coyote.ProtocolHandler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 连接器的实现
 * 实例化 协议处理器
 * 设置外部配置的http 协议
 */
public class Connector {

    /**
     * Coyote Protocol handler class name.
     * Defaults to the Coyote HTTP/1.1 protocolHandler.
     */
    protected String protocolHandlerClassName = "com.jj.tomcat.coyote.http11.Http11NioProtocol";
    private Charset uriCharset = StandardCharsets.UTF_8;//uri编码默认为UTF-8
    /**
     * Coyote protocol handler.
     */
    protected final ProtocolHandler protocolHandler;

    public Connector(String protocol) {
        ProtocolHandler p = null;
        try {
            //加载Http11NioProtocol类并初始化类
            Class<?> clazz = Class.forName(protocolHandlerClassName);
            p = (ProtocolHandler) clazz.getConstructor().newInstance();//实例化
        } catch (Exception e) {

        } finally {
            protocolHandler = p;
        }
    }
}
