package com.isc.eventCenter;

/**
 * 事件通知中心
 * Created by IssacChow on 17/6/6.
 */
public interface IEventCenter {


    /**
     * 发布事件(广播消息)
     * 将Event序列化成MQ 消息，然后广播到MQ中
     *
     * @param event
     */
    void publishEvent(Event event) throws Exception;


    /**
     * 订阅事件(事件监听)
     * 在调用完该方法后需要调用reloadAllListener方法
     * @param listener 事件监听器
     */
    void registerEventListener(IEventListener listener);

    /**
     * 重新加载所有监听器
     */
    void reloadAllListener();

    /**
     * 订阅事件 subscribeEvent的别名
     *
     * @param listener
     */
//    void on(IEventListener listener);

    /**
     * 开始运行
     * 调用后才能开始接受所有消息通知以及发送通知
     */
    void connect();


    void disconnect();


    /**
     * 执行事件(触发事件)
     * 当监听到有广播消息时触发对应事件(通知事件所有监听者)
     *
     * @param event
     */
    //void executeEvent(Event event);


    /**
     * 反序列化事件
     *
     * @param message 消息
     * @return
     */
    //Event unserialize(Object message);

    /**
     * 序列化事件为消息
     *
     * @param event
     */
    //Object serialize(Event event);

}
