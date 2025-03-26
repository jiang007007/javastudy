package com.rpc.core.invoker.route;

import com.rpc.core.register.entity.RegisterInstance;

import java.util.TreeSet;

public abstract class RpcLoadBalance {

    /**
     * @param serviceKey 服务的接口版本号
     * @param instanceTreeSet
     * @return
     */
    public abstract RegisterInstance route(String serviceKey, TreeSet<RegisterInstance> instanceTreeSet);
}
