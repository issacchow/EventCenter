package com.isc.eventCenter.util;

import com.isc.eventCenter.Event;
import com.isc.eventCenter.EventDispatchMode;
import com.isc.eventCenter.IEventListener;
import com.isc.eventCenter.annotation.EventConfig;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by IssacChow on 18/6/8.
 */
public class EventUtil {

    public static Class<Event> getEventClass(IEventListener listener){
       Type t = listener.getClass().getGenericInterfaces()[0];
        ParameterizedType parameterizedType = (ParameterizedType) t;
        Type actualType = parameterizedType.getActualTypeArguments()[0];
        Class<Event> eventClass = (Class<Event>) actualType;
        return eventClass;
    }


    //获取事件分发模式
    public static EventDispatchMode getEventDispatchMode(Class<Event> eventClass) {
        EventConfig config = eventClass.getAnnotation(EventConfig.class);
        if(config==null){
            return null;
        }
        return config.dispatchMode();
    }

    public static String getEventName(Class<Event> eventClass)  {
        EventConfig config = eventClass.getAnnotation(EventConfig.class);
        if(config==null){
            return null;
        }
        return config.eventName();
    }
}
