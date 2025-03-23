package org.jj.until.concurrent;

public interface EventExecutor extends EventExecutorGroup {
    @Override
    EventExecutor next();
}
