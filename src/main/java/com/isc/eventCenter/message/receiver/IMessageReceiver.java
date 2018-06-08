package com.isc.eventCenter.message.receiver;

import com.isc.eventCenter.Event;
import com.isc.eventCenter.IEventListener;

import javax.jms.Message;

/**
 * Created by IssacChow on 18/6/8.
 */
public interface IMessageReceiver {

    /**
     * 接收消息转换成对应的事件
     * @param message 消息
     * @param eventListener 该函数返回事件的监听器
     * @return 返回接收的事件
     */
    Event receive(Message message,IEventListener eventListener) throws Exception;

}
