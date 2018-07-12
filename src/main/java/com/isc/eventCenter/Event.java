package com.isc.eventCenter;

/**
 * 事件抽象类
 * 派生的子类需要打上 @EventConfig 注解
 * Created by IssacChow on 17/6/6.
 */
public abstract class Event {

    /**
     * 事件唯一id
     * 由发布事件方定义事件唯一id
     */
    private String eventId;




    public String getEventId() {
        return eventId;
    }

    public void setEventId(String id) {
        this.eventId = id;
    }









}
