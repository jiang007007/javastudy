package com.jj.tomcat.coyote;

/**
 * 操作钩子.封装回调操作
 */
public interface ActionHook {
    /**
     * 连接器发送的操作码
     * @param actionCode
     * @param param
     */
    void action(ActionCode actionCode,Object param);
}
