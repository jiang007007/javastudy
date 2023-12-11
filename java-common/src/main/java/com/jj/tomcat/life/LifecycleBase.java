package com.jj.tomcat.life;

import com.jj.tomcat.life.listener.LifecycleListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class LifecycleBase implements Lifecycle {


    //注册生命周期事件集
    private final List<LifecycleListener> lifecycleListeners = new CopyOnWriteArrayList<>();


    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);//收集事件
    }


    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycleListeners.toArray(new LifecycleListener[lifecycleListeners.size()]);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }
}
