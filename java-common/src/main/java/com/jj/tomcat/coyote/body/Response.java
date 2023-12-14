package com.jj.tomcat.coyote.body;

import com.jj.tomcat.coyote.ActionHook;

public final class Response {
    Request req;
    final Object notes[] = new Object[32];
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

    public final Object getNode(int pos) {
        return notes[pos];
    }

    public final void setNote(int i, Object value) {
        notes[i] = value;
    }
}
