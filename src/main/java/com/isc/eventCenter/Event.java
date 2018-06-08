package com.isc.eventCenter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.isc.eventCenter.annotation.EventDispatchConfig;
import com.isc.eventCenter.util.EventUtil;

/**
 * 事件抽象类
 * Created by IssacChow on 17/6/6.
 */
@EventDispatchConfig(mode = EventDispatchMode.Broadcast)
public abstract class Event {

    public Event(){
        this.name = EventUtil.getEventName((Class<Event>) this.getClass());
    }

    @SerializedName("event_id")
    @Expose
    private String id;

    @SerializedName("event_name")
    @Expose
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    final  public String getName() {
        return name;
    }







}
