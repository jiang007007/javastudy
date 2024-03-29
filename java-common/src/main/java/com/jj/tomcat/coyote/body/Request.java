package com.jj.tomcat.coyote.body;

import com.jj.tomcat.coyote.ActionCode;
import com.jj.tomcat.coyote.ActionHook;

/**
 * 服务请求封装
 */
public final class Request {

    private Response response;

    private volatile ActionHook hook;

    //维护持有的请求是否有错误
    Exception errorException = null;

    private final RequestInfo reqProcessorMX = new RequestInfo(this);

    //servlet的请求
    private final Object notes[] = new Object[32];

    public void setResponse(Response response) {
        this.response = response;
        response.setRequest(this);
    }

    public void setHook(ActionHook hook) {
        this.hook = hook;
    }

    public RequestInfo getRequestProcessor() {
        return reqProcessorMX;
    }

    public final Object getNote(int pos) {
        return notes[pos];
    }

    public final void setNote(int pos, Object value) {
        notes[pos] = value;
    }

    public void action(ActionCode actionCode, Object param) {
        if (hook != null) {
            if (param == null) {
                hook.action(actionCode, this);
            } else {
                hook.action(actionCode, param);
            }
        }
    }
}
