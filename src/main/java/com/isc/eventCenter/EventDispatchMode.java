package com.isc.eventCenter;

/**
 * 事件分发模式
 * Created by IssacChow on 18/6/7.
 */
public enum EventDispatchMode {

    /**
     * 只分发一次
     */
    Once(0),

    /**
     * 广播式分发
     */
    Broadcast(1);

    private int value = -1;
    EventDispatchMode(int value){
        this.value = value;
    }

}
