package com.jj.tomcat.life;

import java.util.EventObject;

/**
 * 事件对象
 */
public class LifecycleEvent extends EventObject {

    //携带的数据
    private final Object data;
    //生命周期事件类型
    private final String type;


    /**
     * Constructs a prototypical Event.
     *
     * @param lifecycle 组件 发生事件的对象
     * @param type      事件类型.
     * @param data      事件携带的数据.
     */
    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.type = type;
        this.data = data;
    }
}
