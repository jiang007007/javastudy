package com.rpc.core.boot;

import com.rpc.core.util.RpcException;

/**
 * 配置基础类
 */
public class BaseConfig {
    private String env;

    private String appName;

    public BaseConfig(String env, String appName) {
        this.env = env;
        this.appName = appName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void valid() {
        if (env == null || env.trim().isEmpty()) {
            throw new RpcException("xxl-rpc BaseConfig invalid, env not exists.");
        }
        if (appName == null || appName.trim().isEmpty()) {
            throw new RpcException("xxl-rpc BaseConfig invalid, appname not exists");
        }
    }
}
