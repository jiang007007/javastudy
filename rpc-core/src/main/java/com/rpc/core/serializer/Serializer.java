package com.rpc.core.serializer;

import java.util.List;

public interface Serializer {
    void allowPackageList(List<String> packageList);

    <T> byte[] serialize(T obj);

    <T> Object deserialize(byte[] bytes, Class<T> clazz);
}
