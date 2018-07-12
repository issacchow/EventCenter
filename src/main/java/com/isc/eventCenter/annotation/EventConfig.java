package com.isc.eventCenter.annotation;

import com.isc.eventCenter.EventDispatchMode;

import java.lang.annotation.*;

/**
 *
 * Created by IssacChow on 18/6/7.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventConfig {

    /**
     * 事件分发模式,默认为广播模式(即广播事件)
     * @return
     */
    EventDispatchMode dispatchMode() default EventDispatchMode.Broadcast;

    /**
     * 事件名称
     * @return
     */
    String eventName();
}
