package com.isc.eventCenter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

import java.util.Collection;
import java.util.Map;

/**
 * IEventCenter 自动配置类,主要处理逻辑:
 * 1.事件监听器查找
 * 用于查找所有 EventListener类 实例进行订阅事件
 * 2.启动监听
 * Created by IssacChow on 17/6/6.
 */
public class EventCenterAutoConfigure implements ApplicationListener<ContextStartedEvent> {



    public EventCenterAutoConfigure(){

    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();

        IEventCenter eventCenter = applicationContext.getBean(IEventCenter.class);
        if(eventCenter==null)
            return;

        Map<String,IEventListener> listenerMap = applicationContext.getBeansOfType(IEventListener.class);
        Collection<IEventListener> eventListeners = listenerMap.values();

        for(IEventListener listener : eventListeners){
            eventCenter.registerEventListener(listener);
        }

        eventCenter.connect();
    }
}
