package com.jj.tomcat.coyote;

import com.jj.tomcat.coyote.body.Request;
import com.jj.tomcat.coyote.body.Response;


public interface Adapter {

    //调用service method,通知所有监听器
    void service(Request req, Response res) throws Exception;

}
