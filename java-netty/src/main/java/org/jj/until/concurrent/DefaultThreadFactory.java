package org.jj.until.concurrent;

import org.jj.until.StringUtil;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {

    private final AtomicInteger poolId = new AtomicInteger();

    private final AtomicInteger nextId = new AtomicInteger();

    private final String prefix;

    private final boolean demon;

    private final int priority;

    private final ThreadGroup threadGroup;


    public DefaultThreadFactory(Class<?> poolType) {
        this(poolType, false, Thread.NORM_PRIORITY);
    }

    public DefaultThreadFactory(Class<?> poolType, boolean demon, int priority) {
        this(toPoolName(poolType), demon, priority, null);
    }

    public DefaultThreadFactory(String poolName, boolean daemon, int priority, ThreadGroup threadGroup) {
        if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException("线程优先级" + priority + "必需在 Thread.MIN_PRIORITY与Thread.MAX_PRIORITY之间");
        }
        this.prefix = poolName + '_' + poolId.incrementAndGet() + '_';
        this.demon = daemon;
        this.priority = priority;
        this.threadGroup = threadGroup;
    }


    //根据类 生成线程名字
    public static String toPoolName(Class<?> poolType) {
        String poolName = StringUtil.simpleClassName(poolType);
        switch (poolName.length()) {
            case 0:
                return "unknown";
            case 1:
                return poolName.toLowerCase(Locale.US);
            default:
                if (Character.isUpperCase(poolName.charAt(0)) && Character.isLowerCase(poolName.charAt(1))) {
                    return Character.toLowerCase(poolName.charAt(0)) + poolName.substring(1);
                } else {
                    return poolName;
                }
        }
    }


    @Override
    public Thread newThread(Runnable r) {
        Thread t = newThread(FastThreadLocalRunnable.wrap(r), prefix + nextId.getAndIncrement());
        try {
            if (t.isDaemon() != demon) {
                t.setDaemon(demon);
            }
            if (t.getPriority() != priority) {
                t.setPriority(priority);
            }
        } catch (Exception ignored) {

        }
        //1073741824
        return t;
    }


    protected Thread newThread(Runnable r, String name) {
        return new FastThreadLocalThread(threadGroup, r, name);
    }
}
