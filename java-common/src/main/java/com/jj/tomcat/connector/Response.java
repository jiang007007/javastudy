package com.jj.tomcat.connector;

import com.jj.tomcat.connector.http.HttpServletResponse;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Response implements HttpServletResponse {
    @Deprecated
    protected SimpleDateFormat format = null;

    /**
     * The associated output buffer.
     */
    protected OutputBuffer outputBuffer;

    /**
     * The associated output stream.
     */
    protected CoyoteOutputStream outputStream;

    /**
     * The associated writer.
     */
    protected CoyoteWriter writer;
    private com.jj.tomcat.coyote.body.Response coyoteResponse;

    protected Request request = null;
    // ------------------------------------------------------------- Properties

    /**
     * Set the Connector through which this Request was received.
     *  初始化接收请求
     * @param connector The new connector
     */
    public void setConnector(Connector connector) {
        if("AJP/1.3".equals(connector.getProtocol())) {
            // default size to size of one ajp-packet
            outputBuffer = new OutputBuffer(8184);
        } else {
            outputBuffer = new OutputBuffer();
        }
        outputStream = new CoyoteOutputStream(outputBuffer);
        writer = new CoyoteWriter(outputBuffer);
    }

    public void setCoyoteResponse(com.jj.tomcat.coyote.body.Response coyoteResponse) {
        this.coyoteResponse = coyoteResponse;
    }

    public com.jj.tomcat.coyote.body.Response getCoyoteResponse() {
        return coyoteResponse;
    }
    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentLengthLong(long len) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
