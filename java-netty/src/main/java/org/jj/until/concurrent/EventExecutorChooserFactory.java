package org.jj.until.concurrent;

public interface EventExecutorChooserFactory {

    EventExecutorChooser newChooser(EventExecutor[] executors);

    interface EventExecutorChooser {
        EventExecutor next();
    }
}
