package com.isc.eventCenter.message.sender;

import com.isc.eventCenter.Event;

import javax.jms.MessageProducer;
import javax.jms.Session;

/**
 * 消息发送器
 * Created by IssacChow on 18/6/8.
 */
public interface IMessageSender {

    boolean send(Event event,Session session, MessageProducer producer);

}
