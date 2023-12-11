package com.jj.tomcat.connector;

import com.jj.tomcat.coyote.Adapter;
import com.jj.tomcat.coyote.body.Request;
import com.jj.tomcat.coyote.body.Response;

/**
 * 请求处理器的实现，该处理器将处理委托给 Coyote 处理器。
 */
public class CoyoteAdapter implements Adapter {
    private final Connector connector;


    public CoyoteAdapter(Connector connector) {
        super();
        this.connector = connector;
    }

    @Override
    public void service(Request req, Response res) throws Exception {

    }
}
