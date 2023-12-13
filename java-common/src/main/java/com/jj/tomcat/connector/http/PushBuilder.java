package com.jj.tomcat.connector.http;

/**
 * 构建一个基于 HttpServletRequest  的推送请求
 */
public interface PushBuilder {

    PushBuilder method(String method);

    PushBuilder sessionId(String sessionId);

    PushBuilder setHead(String name, String value);

    PushBuilder addHead(String name, String value);

    PushBuilder path(String path);

    String getMethod();

    String getPath();
}
