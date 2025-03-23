package org.jj.until.concurrent;

import org.jj.until.internal.InternalThreadLocalMap;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultithreadEventExecutorGroup extends AbstractEventExecutorGroup {
    private final EventExecutor[] children;//子执行器与线程绑定

    private  Set<EventExecutor> readonlyChildren;

    private final AtomicInteger terminatedChildren = new AtomicInteger();


    private final EventExecutorChooserFactory.EventExecutorChooser chooser;//选择器

    public MultithreadEventExecutorGroup(int nThreads, Executor executor,
                                         EventExecutorChooserFactory chooserFactory, Object... args) {
        if (executor == null) {
            executor = new ThreadPerTaskExecutor(newDefaultThreadFactory());
        }
        children = new EventExecutor[nThreads];
        //根据核心线程数创建具体执行器 NioEventLoop


        //根据执行器的个数创建具体的选择器工厂
        chooser = chooserFactory.newChooser(children);

    }


    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(getClass());
    }

    public static void main(String[] args) {
    for (int i =0 ; i<Integer.MAX_VALUE-8;i++){
        new Thread(FastThreadLocal::new).start();
    }
        FastThreadLocal aNew = new FastThreadLocal();

        System.out.println(aNew.getIndex());

    }

}
