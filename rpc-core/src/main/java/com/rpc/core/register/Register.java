package com.rpc.core.register;

import com.rpc.core.boot.RPCBootstrap;
import com.rpc.core.register.entity.RegisterInstance;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 应用注册
 */
public interface Register {
    /**
     * 1. 服务注册线程   循环线程 维护注册数据在线状态
     * 2. 服务发现线程   long-polling 结合轮询
     */
    void start(RPCBootstrap RPCBootstrap);

    void stop();

    //服务注册
    boolean register(RegisterInstance instance);

    //服务下线
    boolean unregister(RegisterInstance instance);

    //服务发现
    Map<String, TreeSet<RegisterInstance>> discovery(Set<String> appNameList);

    TreeSet<RegisterInstance> discovery(String appName);
}
