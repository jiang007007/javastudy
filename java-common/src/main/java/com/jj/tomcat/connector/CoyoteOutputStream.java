package com.jj.tomcat.connector;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

/**
 * 包装了OutputBuffer
 */
public class CoyoteOutputStream extends ServletOutputStream {

    protected OutputBuffer ob;

    protected CoyoteOutputStream(OutputBuffer ob) {
        this.ob = ob;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {

    }
}
