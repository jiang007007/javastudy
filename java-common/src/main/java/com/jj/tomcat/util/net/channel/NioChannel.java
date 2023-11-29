package com.jj.tomcat.util.net.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SocketChannel;

/**
 * NioChannel 聚合实现类(包装SocketChannel)
 *   整合了ByteChannel ScatteringByteChannel  GatheringByteChannel的功能并实现
 */
public class NioChannel implements ByteChannel, ScatteringByteChannel, GatheringByteChannel {

    private SocketChannel sc;
    private SocketWrapperBase<NioChannel> socketWrapper;


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
