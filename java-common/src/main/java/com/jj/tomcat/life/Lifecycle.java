package com.jj.tomcat.life;

import com.jj.tomcat.life.listener.LifecycleListener;

/**
 * 通过的声明周期接口
 * 定义常量和添加观察者方法
 */
public interface Lifecycle {

    String BEFORE_INIT_EVENT = "before_init";


    String AFTER_INIT_EVENT = "after_init";


    String START_EVENT = "start";


    String BEFORE_START_EVENT = "before_start";


    String AFTER_START_EVENT = "after_start";


    String STOP_EVENT = "stop";


    String BEFORE_STOP_EVENT = "before_stop";

    String AFTER_STOP_EVENT = "after_stop";

    String AFTER_DESTROY_EVENT = "after_destroy";


    String BEFORE_DESTROY_EVENT = "before_destroy";


    String PERIODIC_EVENT = "periodic";

    String CONFIGURE_START_EVENT = "configure_start";

    String CONFIGURE_STOP_EVENT = "configure_stop";


    //添加监听事件
    void addLifecycleListener(LifecycleListener listener);

    //返回所有监听事件
    LifecycleListener[] findLifecycleListeners();

    void removeLifecycleListener(LifecycleListener listener);

    void init() throws Exception;

    void start() throws Exception;

    void stop() throws Exception;

    void destroy() throws Exception;

}
