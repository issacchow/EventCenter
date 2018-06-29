package com.isc.eventCenter;

/**
 * Created by IssacChow on 17/6/6.
 */
public interface IEventListener<EventType extends Event> {


    /**
     * 监听器名称
     * @return
     */
    String getName();

    /**
     * 执行事件过程
     * @param eventCenter
     * @param event
     * @return 返回一个值表示是否完成事件的消费,true 表示消费成功,否则失败
     */
    boolean onExecuteEvent(IEventCenter eventCenter, EventType event);
}
