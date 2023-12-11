package com.jj.tomcat.coyote.body;


import java.util.ArrayList;

public class RequestGroupInfo {
    private final ArrayList<RequestInfo> processors = new ArrayList<>();
    private long deadMaxTime = 0;
    private long deadProcessingTime = 0;
    private int deadRequestCount = 0;
    private int deadErrorCount = 0;
    private long deadBytesReceived = 0;
    private long deadBytesSent = 0;
}
