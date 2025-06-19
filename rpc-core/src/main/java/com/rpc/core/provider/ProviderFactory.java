package com.rpc.core.provider;

import com.rpc.core.boot.RPCBootstrap;
import com.rpc.core.register.entity.RegisterInstance;
import com.rpc.core.remoting.Server;
import com.rpc.core.serializer.Serializer;
import com.rpc.core.util.IpUtil;
import com.rpc.core.util.NetUtil;
import com.rpc.core.util.RpcException;

import java.util.concurrent.Callable;

public class ProviderFactory {

    private RPCBootstrap RPCBootstrap;

    private Server server;

    private Serializer serializer;

    public ProviderFactory(RPCBootstrap RPCBootstrap) {
        this.RPCBootstrap = RPCBootstrap;
    }

    //开启服务
    public void start() throws Exception {
        if (RPCBootstrap.getProviderConfig().getServer() == null) {
            throw new RpcException("服务不能为空!");
        }
        if (RPCBootstrap.getProviderConfig().getSerializer() == null) {
            throw new RpcException("序列化不能为空!");
        }

        //设置线程池的核心线程数和最大线程数
        if (!(RPCBootstrap.getProviderConfig().getCorePoolSize() > 0
                && RPCBootstrap.getProviderConfig().getMaxPoolSize() > 0
                && RPCBootstrap.getProviderConfig().getMaxPoolSize() >= RPCBootstrap.getProviderConfig().getCorePoolSize()
        )) {
            RPCBootstrap.getProviderConfig().setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
            RPCBootstrap.getProviderConfig().setMaxPoolSize(300);
        }

        //设置服务器的ip和端口
        if (RPCBootstrap.getProviderConfig().getPort() <= 0) {
            RPCBootstrap.getProviderConfig().setPort(8888);
        }
        String ip = IpUtil.getIp();
        if (RPCBootstrap.getProviderConfig().getAddress() == null ||
                RPCBootstrap.getProviderConfig().getAddress().isEmpty()
        ) {
            String address = IpUtil.getIpPort(ip, RPCBootstrap.getProviderConfig().getPort());
            RPCBootstrap.getProviderConfig().setAddress(address);
        }

        //检测地址是否为使用了
        if (NetUtil.isPortUsed(RPCBootstrap.getProviderConfig().getPort())) {
            throw new RpcException("服务提供者的端口" + RPCBootstrap.getProviderConfig().getPort() + "已被使用");
        }
        //初始化序列化类和服务器类
        this.serializer = RPCBootstrap.getProviderConfig().getSerializer().newInstance();
        this.serializer.allowPackageList(RPCBootstrap.getProviderConfig().getSerializerAllowPackageList());
        this.server = RPCBootstrap.getProviderConfig().getServer().newInstance();

        //设置回调
        this.server.setStartedCallback(new Callable<Void>() {
            @Override
            public Void call() {
                //服务器启动后 注册实例到配置中心
                if (RPCBootstrap.getRegister() == null) {
                    return null;
                }
                RegisterInstance registerInstance = new RegisterInstance();
                registerInstance.setEnv(RPCBootstrap.getBaseConfig().getEnv());
                registerInstance.setAppName(RPCBootstrap.getBaseConfig().getAppName());
                registerInstance.setIp(ip);
                registerInstance.setPort(RPCBootstrap.getProviderConfig().getPort());
                RPCBootstrap.getRegister().register(registerInstance);
                return null;
            }
        });
        this.server.setStopedCallback(new Callable<Void>() {
            @Override
            public Void call() {
                if (RPCBootstrap.getRegister() == null) {
                    return null;
                }
                RegisterInstance registerInstance = new RegisterInstance();
                registerInstance.setEnv(RPCBootstrap.getBaseConfig().getEnv());
                registerInstance.setAppName(RPCBootstrap.getBaseConfig().getAppName());
                registerInstance.setIp(ip);
                registerInstance.setPort(RPCBootstrap.getProviderConfig().getPort());
                RPCBootstrap.getRegister().unregister(registerInstance);
                return null;
            }
        });

        //启动服务
        server.start(RPCBootstrap);
    }

}
