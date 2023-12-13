package com.jj.tomcat.connector.http;

public interface HttpServletMapping {
    String getPattern();

    String getServletName();

    MappingMatch getMappingMatch();
}
