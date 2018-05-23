package com.isc.eventCenter;

/**
 * 简单事件,只有一个文本内容
 * Created by IssacChow on 17/10/31.
 */
public abstract class SimpleEvent extends Event {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent(String format,Object ...args){
        this.content = String.format(format,args);
    }
}
