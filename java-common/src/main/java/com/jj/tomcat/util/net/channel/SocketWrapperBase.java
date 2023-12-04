package com.jj.tomcat.util.net.channel;

import com.jj.tomcat.util.net.AbstractEndpoint;
import com.jj.tomcat.util.net.buffer.SocketBufferHandler;

public abstract class SocketWrapperBase<E> {

    private final E socket;//NioChannel(对SocketChannel的包装)

    private final AbstractEndpoint<E> endpoint;

    protected volatile SocketBufferHandler socketBufferHandler;

    private volatile long readTimeout = -1;
    private volatile long writeTimeout = -1;

    private volatile int keepAliveLeft = 100;


    public SocketWrapperBase(E socket, AbstractEndpoint<E> endpoint) {
        this.socket = socket;
        this.endpoint = endpoint;
    }

    public abstract boolean isClosed();

    /**
     * Set the timeout for reading. Values of zero or less will be changed to
     * -1.
     *
     * @param readTimeout The timeout in milliseconds. A value of -1 indicates
     *                    an infinite timeout.
     */
    public void setReadTimeout(long readTimeout) {
        if (readTimeout > 0) {
            this.readTimeout = readTimeout;
        } else {
            this.readTimeout = -1;
        }
    }

    public long getReadTimeout() {
        return this.readTimeout;
    }


    /**
     * Set the timeout for writing. Values of zero or less will be changed to
     * -1.
     *
     * @param writeTimeout The timeout in milliseconds. A value of zero or less
     *                     indicates an infinite timeout.
     */
    public void setWriteTimeout(long writeTimeout) {
        if (writeTimeout > 0) {
            this.writeTimeout = writeTimeout;
        } else {
            this.writeTimeout = -1;
        }
    }

    public long getWriteTimeout() {
        return this.writeTimeout;
    }

    public void setKeepAliveLeft(int keepAliveLeft) {
        this.keepAliveLeft = keepAliveLeft;
    }

    public int decrementKeepAlive() {
        return (--keepAliveLeft);
    }

    public E getSocket() {
        return socket;
    }


    public AbstractEndpoint<E> getEndpoint() {
        return endpoint;
    }
}
