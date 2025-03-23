package org.jj.until.concurrent;

public class FastThreadLocalRunnable implements Runnable {

    private Runnable runnable;

    public FastThreadLocalRunnable(Runnable runnable){
        this.runnable = runnable;
    }


    @Override
    public void run() {
        try {
            runnable.run();//调用Runnable的run方法
        }finally {
            FastThreadLocal.removeAll();
        }

    }

    //把Runnable包装为FastThreadLocalRunnable对象
    static Runnable wrap(Runnable runnable){
        return runnable instanceof FastThreadLocalRunnable? runnable:new FastThreadLocalRunnable(runnable);
    }
}
