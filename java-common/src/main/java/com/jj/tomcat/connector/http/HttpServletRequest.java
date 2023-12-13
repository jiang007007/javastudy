package com.jj.tomcat.connector.http;

public interface HttpServletRequest extends javax.servlet.http.HttpServletRequest {

    HttpServletMapping getHttpServletMapping();

    PushBuilder newPushBuilder();
}
