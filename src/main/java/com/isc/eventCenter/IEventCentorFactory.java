package com.isc.eventCenter;

/**
 * Created by IssacChow on 18/5/24.
 */
public interface IEventCentorFactory {

    IEventCenter build();
    IEventCenter build(String propertiesFile);
}
