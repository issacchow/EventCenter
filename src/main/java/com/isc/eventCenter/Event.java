package com.isc.eventCenter;

import com.isc.eventCenter.annotation.EventConfig;
import com.isc.eventCenter.util.EventUtil;

/**
 * 事件抽象类
 * Created by IssacChow on 17/6/6.
 */
@EventConfig(mode = EventDispatchMode.Broadcast)
public abstract class Event {

    public Event(){
        this.eventName = EventUtil.getEventName((Class<Event>) this.getClass());
    }


    private String eventId;


    private String eventName;


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String id) {
        this.eventId = id;
    }


    final  public String getEventName() {
        return eventName;
    }







}
