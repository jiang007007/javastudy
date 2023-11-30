package com.jj.tomcat.util.net.channel;

import com.jj.tomcat.util.net.NioEndpoint;
import com.jj.tomcat.util.net.buffer.SocketBufferHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * NioChannel 聚合实现类(包装SocketChannel)
 * 整合了ByteChannel ScatteringByteChannel  GatheringByteChannel的功能并实现
 */
public class NioChannel implements ByteChannel, ScatteringByteChannel, GatheringByteChannel {

    protected SocketChannel sc;
    protected SocketWrapperBase<NioChannel> socketWrapper;

    protected final SocketBufferHandler bufferHandler;

    protected  NioEndpoint.Poller poller;


    public NioChannel(SocketChannel channel, SocketBufferHandler bufferHandler) {
        this.sc = channel;
        this.bufferHandler = bufferHandler;
    }

    public SocketChannel getIOChannel() {
        return sc;
    }
    public void setPoller(NioEndpoint.Poller poller) {
        this.poller = poller;
    }

    public NioEndpoint.Poller getPoller() {
       return poller;
    }

    void setSocketWrapper(SocketWrapperBase<NioChannel> socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    public SocketWrapperBase<NioChannel> getSocketWrapper() {
        return socketWrapper;
    }

    public SocketBufferHandler getBufHandler() {
        return bufferHandler;
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        return 0;
    }

    @Override
    public long write(ByteBuffer[] srcs) throws IOException {
        return 0;
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        return 0;
    }

    @Override
    public long read(ByteBuffer[] dsts) throws IOException {
        return 0;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return 0;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return 0;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() throws IOException {

    }
}
