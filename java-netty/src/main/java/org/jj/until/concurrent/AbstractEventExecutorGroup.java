package org.jj.until.concurrent;

public abstract class AbstractEventExecutorGroup implements EventExecutorGroup {

    @Override
    public Future<?> submit(Runnable task) {
        return next().submit(task);
    }
}
