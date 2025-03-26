package com.rpc.core.serializer.impl;

import com.alibaba.fastjson2.JSONReader;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianOutput;
import com.rpc.core.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hessian implements Serializer {
    private Set<String> pkgList = new HashSet<>(Arrays.asList("com", "org", "io"));

    @Override
    public void allowPackageList(List<String> packageList) {
        if (packageList != null && !packageList.isEmpty()) {
            pkgList = new HashSet<>(packageList);
        }
    }

    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            Hessian2Output hessianOutput = new Hessian2Output(bos);
            hessianOutput.writeObject(obj);
            hessianOutput.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            SafeHessianInput input = new SafeHessianInput(bais, pkgList);
            return input.readObject(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
