package com.rpc.core.serializer.impl;

import com.caucho.hessian.io.Hessian2Input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

//新增白名单
public class SafeHessianInput extends Hessian2Input {
    private final Set<String> allowPackege;

    public SafeHessianInput(InputStream inputStream, Set<String> allowPackege) {
        super(inputStream);
        this.allowPackege = allowPackege;
    }

    @Override
    public Object readObject() throws IOException {
        Object obj = super.readObject();
        if (obj != null && allowPackege.stream().noneMatch(e->e.startsWith(obj.getClass().getName()))) {
            throw new RuntimeException();
        }
        return obj;
    }
}
