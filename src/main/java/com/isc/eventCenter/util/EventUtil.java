package com.isc.eventCenter.util;

import com.isc.eventCenter.Event;
import com.isc.eventCenter.IEventListener;

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


    public static String getEventName(Class<Event> eventClass){
        return eventClass.getSimpleName();
    }
}
