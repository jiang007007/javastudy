package com.rpc.core.invoker.register.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 注册实例包含:
 *  环境
 *  应用名
 *  ip
 *  端口
 *  扩展信息
 */
public class RegisterInstance implements Serializable, Comparable {

    private String env;

    //应用名(唯一)
    private String appName;

    //注册的机器Ip
    private String ip;

    private int port;

    //扩展信息
    private String extendInfo;

    public String getUniqueKey() {
        return ip + ":" + port;
    }

    public RegisterInstance() {

    }

    public RegisterInstance(String env, String appName, String ip, int port, String extendInfo) {
        this.env = env;
        this.appName = appName;
        this.ip = ip;
        this.port = port;
        this.extendInfo = extendInfo;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    //重写equals方法
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        RegisterInstance that = (RegisterInstance) object;
        return Objects.equals(env, that.env)
                && Objects.equals(appName, that.appName)
                && Objects.equals(ip, that.ip)
                && port == that.port
                && Objects.equals(extendInfo, that.extendInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(env, appName, ip, port, extendInfo);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof RegisterInstance) {
            RegisterInstance that = (RegisterInstance) o;
            return this.getUniqueKey().compareTo(that.getUniqueKey());
        }
        return 0;
    }
}
