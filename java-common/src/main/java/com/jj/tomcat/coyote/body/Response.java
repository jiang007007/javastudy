package com.jj.tomcat.coyote.body;

import com.jj.tomcat.coyote.ActionHook;

public final class Response {
    Request req;

    volatile ActionHook hook;

    public Request getRequest() {
        return req;
    }

    public void setRequest(Request req) {
        this.req = req;
    }

    public void setHook(ActionHook hook) {
        this.hook = hook;
    }
}
