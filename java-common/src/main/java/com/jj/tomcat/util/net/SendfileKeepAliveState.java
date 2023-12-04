package com.jj.tomcat.util.net;

public enum SendfileKeepAliveState {
    NONE,

    //keep正在使用,并且输入缓冲区有数据
    PIPELINED,

    OPEN
}
