package org.jj.until.concurrent;

public interface Future <V> extends java.util.concurrent.Future<V> {
    boolean isSuccess();


}
