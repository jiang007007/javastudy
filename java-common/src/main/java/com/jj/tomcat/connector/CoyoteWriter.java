package com.jj.tomcat.connector;

import java.io.PrintWriter;
import java.io.Writer;

public class CoyoteWriter extends PrintWriter {
    protected OutputBuffer ob;
    protected boolean error = false;

    public CoyoteWriter(Writer out) {
        super(out);
        this.ob = ob;
    }
}
