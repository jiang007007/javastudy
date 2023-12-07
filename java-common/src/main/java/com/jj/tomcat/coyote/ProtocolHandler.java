package com.jj.tomcat.coyote;

import java.util.concurrent.Executor;

/**
 * 1 提供了与协议处理相关的适配器
 * 2 模块初始化 启动  挂起 停止  销毁等方法
 */
public interface ProtocolHandler {
    Adapter getAdapter();

    void setAdapter();

    Executor getExecutor();

}
