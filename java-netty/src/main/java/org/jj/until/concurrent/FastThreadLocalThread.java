package org.jj.until.concurrent;

import org.jj.until.internal.InternalThreadLocalMap;

public class FastThreadLocalThread extends Thread {

    private InternalThreadLocalMap threadLocalMap;
    private final boolean cleanupFastThreadLocals;

    public FastThreadLocalThread(ThreadGroup group, Runnable target, String name) {
        super(group, FastThreadLocalRunnable.wrap(target), name);
        cleanupFastThreadLocals = true;
    }


    public final InternalThreadLocalMap threadLocalMap() {

        return threadLocalMap;
    }

    public final void setThreadLocalMap(InternalThreadLocalMap threadLocalMap) {
        this.threadLocalMap = threadLocalMap;
    }
}
