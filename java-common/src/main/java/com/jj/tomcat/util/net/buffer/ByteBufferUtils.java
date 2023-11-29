package com.jj.tomcat.util.net.buffer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class ByteBufferUtils {

    private static final Method cleanerMethod;
    private static final Method cleanMethod;

    static {
        ByteBuffer tempBuffer = ByteBuffer.allocateDirect(0);
        Method cleanerMethodLocal;
        Method cleanMethodLocal;
        //默认jdk1.8及以下
        try {
            //获取到DirectByteBuffer的cleaner方法
            cleanerMethodLocal = tempBuffer.getClass().getMethod("cleaner");
            cleanerMethodLocal.setAccessible(true);
            //反射调用cleaner方法 获取Cleaner方法
            Object cleanerObject = cleanerMethodLocal.invoke(tempBuffer);
            //获取Cleaner类中的clean方法  该方法实现了Deallocator的run方法调用
            cleanMethodLocal = cleanerObject.getClass().getMethod("clean");
        }catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException e){
            cleanerMethodLocal = null;
            cleanMethodLocal = null;
        }
        cleanerMethod = cleanerMethodLocal;
        cleanMethod = cleanMethodLocal;

    }

    public static void cleanDirectBuffer(ByteBuffer buf){
        if (cleanMethod!= null){
            try {
                //通过unsafe.free释放
                cleanMethod.invoke(cleanerMethod.invoke(buf));
            }catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | SecurityException e){
                //打印日志
            }
        }
    }
}
