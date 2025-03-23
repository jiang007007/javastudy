package org.jj.until.concurrent;

import java.util.concurrent.ScheduledExecutorService;

public interface EventExecutorGroup extends ScheduledExecutorService, Iterable<EventExecutor> {
    EventExecutor next();
    @Override
    Future<?> submit(Runnable task);

}
