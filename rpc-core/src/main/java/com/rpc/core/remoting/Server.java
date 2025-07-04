package com.rpc.core.remoting;

import com.rpc.core.boot.RPCBootstrap;

import java.util.concurrent.Callable;

public abstract class Server {
    //设置回调
    private Callable<Void> startedCallback;

    private Callable<Void> stopedCallback;
    public abstract void start(RPCBootstrap RPCBootstrap)throws Exception;



    public void onStarted() {
        if (startedCallback != null) {
            try {
                startedCallback.call();
            } catch (Exception e) {

            }
        }
    }


    public void onStoped() {
        if (stopedCallback != null) {
            try {
                stopedCallback.call();
            } catch (Exception e) {

            }
        }
    }

    public void setStartedCallback(Callable<Void> startedCallback) {
        this.startedCallback = startedCallback;
    }

    public void setStopedCallback(Callable<Void> stopedCallback) {
        this.stopedCallback = stopedCallback;
    }
}
