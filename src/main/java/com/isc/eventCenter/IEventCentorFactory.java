package com.isc.eventCenter;

import org.springframework.core.io.Resource;

/**
 * Created by IssacChow on 18/5/24.
 */
public interface IEventCentorFactory {

    IEventCenter build();
    IEventCenter build(Resource resource);
    IEventCenter build(String resourceName);
}
