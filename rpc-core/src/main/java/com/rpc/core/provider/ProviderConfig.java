package com.rpc.core.provider;

import com.rpc.core.remoting.Server;
import com.rpc.core.remoting.impl.NettyServer;
import com.rpc.core.serializer.Serializer;
import com.rpc.core.serializer.impl.JsonbSerializer;

import java.util.List;

public class ProviderConfig {
    private boolean open = true;

    private Class<? extends Server> server = NettyServer.class;

    //默认序列化实例
    private Class<? extends Serializer> serializer = JsonbSerializer.class;

    private List<String> serializerAllowPackageList;

    //服务的端口号
    private int port = 8000;

    //默认线程池核心线程
    private int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    private int maxPoolSize = 300;
    private String address;

    public ProviderConfig() {

    }

    public ProviderConfig(Class<? extends Server> server,
                          Class<? extends Serializer> serializer,
                          List<String> serializerAllowPackageList,
                          int port,
                          int corePoolSize,
                          int maxPoolSize,
                          String address) {
        this.open = true;
        this.server = server;
        this.serializer = serializer;
        this.serializerAllowPackageList = serializerAllowPackageList;
        this.port = port;
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.address = address;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Class<? extends Server> getServer() {
        return server;
    }

    public void setServer(Class<? extends Server> server) {
        this.server = server;
    }

    public Class<? extends Serializer> getSerializer() {
        return serializer;
    }

    public void setSerializer(Class<? extends Serializer> serializer) {
        this.serializer = serializer;
    }

    public List<String> getSerializerAllowPackageList() {
        return serializerAllowPackageList;
    }

    public void setSerializerAllowPackageList(List<String> serializerAllowPackageList) {
        this.serializerAllowPackageList = serializerAllowPackageList;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
