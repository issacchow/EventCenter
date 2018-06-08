package com.isc.eventCenter.impl.activemq.annotation;

import java.lang.annotation.*;

/**
 * 独占监听器,请参考activemq "Exclusive Consumer"
 * Created by IssacChow on 18/6/7.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExclusiveListener {
}