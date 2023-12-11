package com.jj.tomcat.life.listener;

import com.jj.tomcat.life.LifecycleEvent;

public interface LifecycleListener {
    void lifecycleEvent(LifecycleEvent event);
}
