package com.rpc.core.serializer.impl;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.rpc.core.serializer.Serializer;

import java.util.List;

public class JsonbSerializer implements Serializer {

    //设置序列化 反序列的包
    private static Filter autoTypeBeforeHandler = JSONReader.autoTypeFilter("com", "org", "io");

    @Override
    public void allowPackageList(List<String> packageList) {
        if (packageList != null && !packageList.isEmpty()) {
            autoTypeBeforeHandler = JSONReader.autoTypeFilter(packageList.toArray(new String[0]));
        }
    }

    @Override
    public <T> byte[] serialize(T obj) {
        return JSONB.toBytes(obj, JSONWriter.Feature.WriteClassName);
    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        return JSONB.parseObject(bytes,clazz,autoTypeBeforeHandler,JSONReader.Feature.SupportClassForName);
    }
}
