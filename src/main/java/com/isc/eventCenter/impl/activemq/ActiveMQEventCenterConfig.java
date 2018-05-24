package com.isc.eventCenter.impl.activemq;

import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;

/**
 * Created by IssacChow on 18/5/24.
 */
public class ActiveMQEventCenterConfig {
    private RedeliveryPolicy redeliveryPolicy;
    private ActiveMQPrefetchPolicy prefetchPolicy;

    public ActiveMQEventCenterConfig(){
        redeliveryPolicy = new RedeliveryPolicy();
        prefetchPolicy = new ActiveMQPrefetchPolicy();
    }

    public RedeliveryPolicy getRedeliveryPolicy() {
        return redeliveryPolicy;
    }

    public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
        this.redeliveryPolicy = redeliveryPolicy;
    }

    public ActiveMQPrefetchPolicy getPrefetchPolicy() {
        return prefetchPolicy;
    }

    public void setPrefetchPolicy(ActiveMQPrefetchPolicy prefetchPolicy) {
        this.prefetchPolicy = prefetchPolicy;
    }
}
