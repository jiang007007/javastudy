package com.rpc.core.provider;

import com.rpc.core.boot.Bootstrap;
import com.rpc.core.register.entity.RegisterInstance;
import com.rpc.core.remoting.Server;
import com.rpc.core.serializer.Serializer;
import com.rpc.core.util.IpUtil;
import com.rpc.core.util.NetUtil;
import com.rpc.core.util.RpcException;

import java.util.concurrent.Callable;

public class ProviderFactory {

    private Bootstrap bootstrap;

    private Server server;

    private Serializer serializer;

    public ProviderFactory(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    //开启服务
    public void start() throws Exception {
        if (bootstrap.getProviderConfig().getServer() == null) {
            throw new RpcException("服务不能为空!");
        }
        if (bootstrap.getProviderConfig().getSerializer() == null) {
            throw new RpcException("序列化不能为空!");
        }

        //设置线程池的核心线程数和最大线程数
        if (!(bootstrap.getProviderConfig().getCorePoolSize() > 0
                && bootstrap.getProviderConfig().getMaxPoolSize() > 0
                && bootstrap.getProviderConfig().getMaxPoolSize() >= bootstrap.getProviderConfig().getCorePoolSize()
        )) {
            bootstrap.getProviderConfig().setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
            bootstrap.getProviderConfig().setMaxPoolSize(300);
        }

        //设置服务器的ip和端口
        if (bootstrap.getProviderConfig().getPort() <= 0) {
            bootstrap.getProviderConfig().setPort(8888);
        }
        String ip = IpUtil.getIp();
        if (bootstrap.getProviderConfig().getAddress() == null ||
                bootstrap.getProviderConfig().getAddress().isEmpty()
        ) {
            String address = IpUtil.getIpPort(ip, bootstrap.getProviderConfig().getPort());
            bootstrap.getProviderConfig().setAddress(address);
        }

        //检测地址是否为使用了
        if (NetUtil.isPortUsed(bootstrap.getProviderConfig().getPort())) {
            throw new RpcException("服务提供者的端口" + bootstrap.getProviderConfig().getPort() + "已被使用");
        }
        //初始化序列化类和服务器类
        this.serializer = bootstrap.getProviderConfig().getSerializer().newInstance();
        this.serializer.allowPackageList(bootstrap.getProviderConfig().getSerializerAllowPackageList());
        this.server = bootstrap.getProviderConfig().getServer().newInstance();

        //设置回调
        this.server.setStartedCallback(new Callable<Void>() {
            @Override
            public Void call() {
                //服务器启动后 注册实例到配置中心
                if (bootstrap.getRegister() == null) {
                    return null;
                }
                RegisterInstance registerInstance = new RegisterInstance();
                registerInstance.setEnv(bootstrap.getBaseConfig().getEnv());
                registerInstance.setAppName(bootstrap.getBaseConfig().getAppName());
                registerInstance.setIp(ip);
                registerInstance.setPort(bootstrap.getProviderConfig().getPort());
                bootstrap.getRegister().register(registerInstance);
                return null;
            }
        });
    }

}
