package com.jj.tomcat.connector;

import com.jj.tomcat.coyote.body.Response;

import java.io.IOException;
import java.io.Writer;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class OutputBuffer extends Writer {


    /**
     * Default buffer size.
     */
    private final int defaultBufferSize;

    /**
     * The byte buffer.
     */
    private ByteBuffer bb;


    /**
     * The char buffer.
     */
    private final CharBuffer cb;

    /**
     * Suspended flag. All output bytes will be swallowed if this is true.
     */
    private volatile boolean suspended = false;


    /**
     * Flag which indicates if the output buffer is closed.
     */
    private volatile boolean closed = false;


    public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;


    /**
     * Default constructor. Allocate the buffer with the default buffer size.
     */
    public OutputBuffer() {

        this(DEFAULT_BUFFER_SIZE);

    }
    /**
     * Associated Coyote response.
     */
    private Response coyoteResponse;
    public OutputBuffer(int size) {
        defaultBufferSize = size;
        bb = ByteBuffer.allocate(size);
        clear(bb);
        cb = CharBuffer.allocate(size);
        clear(cb);
    }
    private void clear(Buffer buffer) {
        buffer.rewind().limit(0);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
