package com.isc.eventCenter.serialize;

import com.isc.eventCenter.Event;

/**
 * 序列化接口
 * Created by IssacChow on 18/6/8.
 */
public interface ISerializer {

    Object serialize(Event event);

    Event deserialize(Object serialzedObj);
}
