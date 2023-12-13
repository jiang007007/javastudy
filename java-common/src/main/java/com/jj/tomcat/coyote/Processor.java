package com.jj.tomcat.coyote;

import com.jj.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
import com.jj.tomcat.util.net.SocketEvent;
import com.jj.tomcat.util.net.channel.NioChannel;
import com.jj.tomcat.util.net.channel.SocketWrapperBase;

import java.io.IOException;

/**
 * 协议处理器的通用接口
 */
public interface Processor {
    /**
     * 处理连接。每当发生事件（例如，更多数据到达）时，都会调用此函数，该事件允许继续处理当前未处理的连接
     * <p>
     * 连接真正处理方法
     *
     * @param socketWrapper 封装的NioChannel对象
     * @param status        触发的连接事件
     */
    SocketState process(SocketWrapperBase<?> socketWrapper, SocketEvent status) throws IOException;



}
