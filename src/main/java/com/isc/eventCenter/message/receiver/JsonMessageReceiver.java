package com.isc.eventCenter.message.receiver;

import com.google.gson.Gson;
import com.isc.eventCenter.Event;
import com.isc.eventCenter.IEventListener;
import com.isc.eventCenter.util.EventUtil;

import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Created by IssacChow on 18/6/8.
 */
public class JsonMessageReceiver implements IMessageReceiver {

    @Override
    public Event receive(Message message,IEventListener eventListener) throws Exception{
        TextMessage msg = (TextMessage) message;
        String json = msg.getText();

        Gson gson = new Gson();
        Class<Event> eventClass = EventUtil.getEventClass(eventListener);
        Event event = gson.fromJson(json, eventClass);
        return event;

    }
}
