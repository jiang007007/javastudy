package com.jj.tomcat.util.net;

public abstract class SendfileDataBase {
    public SendfileKeepAliveState keepAliveState = SendfileKeepAliveState.NONE;

    public final String fileName;

    //写位置
    public long pos;

    //剩余写入的数据
    public long length;

    public SendfileDataBase(String fileName, long pos, long length) {
        this.fileName = fileName;
        this.pos = pos;
        this.length = length;
    }
}
