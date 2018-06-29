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
    EventDispatchMode mode() default EventDispatchMode.Broadcast;
}
