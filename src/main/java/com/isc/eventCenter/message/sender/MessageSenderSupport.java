package com.isc.eventCenter.message.sender;

import com.isc.eventCenter.Event;
import com.isc.eventCenter.serialize.ISerializer;

import javax.jms.MessageProducer;

/**
 * Created by IssacChow on 18/6/8.
 */
public abstract class MessageSenderSupport {
    private ISerializer serializer;

    public ISerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(ISerializer serializer) {
        this.serializer = serializer;
    }

}
