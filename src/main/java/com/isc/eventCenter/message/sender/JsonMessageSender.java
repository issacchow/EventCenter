package com.isc.eventCenter.message.sender;

import com.google.gson.Gson;
import com.isc.eventCenter.Event;

import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Created by IssacChow on 18/6/8.
 */
public class JsonMessageSender extends MessageSenderSupport implements IMessageSender  {


    @Override
    public boolean send(Event event,Session session, MessageProducer producer) {
        Gson gson = new Gson();
        String json = gson.toJson(event);
        TextMessage textMessage = null;
        try {
            textMessage = session.createTextMessage();
            textMessage.setText(json);
            producer.send(textMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


}
