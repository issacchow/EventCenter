package com.isc.eventCenter.message;

import com.isc.eventCenter.message.receiver.IMessageReceiver;
import com.isc.eventCenter.message.receiver.JsonMessageReceiver;
import com.isc.eventCenter.message.sender.IMessageSender;
import com.isc.eventCenter.message.sender.JsonMessageSender;

/**
 * 消息处理器工厂
 * Created by IssacChow on 18/6/8.
 */
public class MessageProcessorFactory {

    private static IMessageReceiver messageReceiver = new JsonMessageReceiver();
    private static IMessageSender messageSender = new JsonMessageSender();


    public static IMessageReceiver getMessageReceiver(){
        return messageReceiver;
    }

    public static IMessageSender getMessageSender(){
        return messageSender;
    }
}
