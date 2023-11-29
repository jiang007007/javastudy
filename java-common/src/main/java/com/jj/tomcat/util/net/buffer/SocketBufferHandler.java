package com.jj.tomcat.util.net.buffer;

import java.nio.ByteBuffer;

public class SocketBufferHandler {

    private volatile boolean readBufferConfiguredForWrite = true;

    private volatile boolean writeBufferConfiguredForWrite = true;

    private volatile ByteBuffer readBuffer;
    private volatile ByteBuffer writeBuffer;
    //是否使用堆外内存buffer
    private final boolean direct;

    public SocketBufferHandler(int readBufferSize, int writeBufferSize, boolean direct) {
        this.direct = direct;
        if (direct) {
            readBuffer = ByteBuffer.allocateDirect(readBufferSize);
            writeBuffer = ByteBuffer.allocateDirect(writeBufferSize);
        } else {
            readBuffer = ByteBuffer.allocate(readBufferSize);
            writeBuffer = ByteBuffer.allocate(writeBufferSize);
        }

    }

    public void free() {
        if (direct) {
            ByteBufferUtils.cleanDirectBuffer(readBuffer);
            ByteBufferUtils.cleanDirectBuffer(writeBuffer);
        }
    }

}
