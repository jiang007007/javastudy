package com.rpc.core.boot;

import com.rpc.core.provider.ProviderConfig;
import com.rpc.core.register.Register;

public class Bootstrap {

    private BaseConfig baseConfig;

    private ProviderConfig providerConfig;
    private volatile Register register;


    public BaseConfig getBaseConfig() {
        return baseConfig;
    }

    public void setBaseConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    public ProviderConfig getProviderConfig() {
        return providerConfig;
    }

    public void setProviderConfig(ProviderConfig providerConfig) {
        this.providerConfig = providerConfig;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
}
