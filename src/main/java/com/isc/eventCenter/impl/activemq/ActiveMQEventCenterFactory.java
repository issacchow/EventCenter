package com.isc.eventCenter.impl.activemq;

import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventCentorFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;

import java.io.*;
import java.util.Properties;

/**
 * Created by IssacChow on 18/5/24.
 */
public class ActiveMQEventCenterFactory implements IEventCentorFactory {

    @Override
    public IEventCenter build() {
        ActiveMQEventCenter center = new ActiveMQEventCenter();
        return center;
    }

    @Override
    public IEventCenter build(String propertiesFile) {

        try {
            InputStream inputStream = new FileInputStream(new File(propertiesFile));
            Properties properties = new Properties();
            try {
                properties.load(inputStream);

                PropertyValues propertyValues = new MutablePropertyValues(properties);

                ActiveMQEventCenter center = new ActiveMQEventCenter();
                BeanWrapper configWrapper = new BeanWrapperImpl(new ActiveMQEventCenterConfig());
                configWrapper.setPropertyValues(propertyValues);
                ActiveMQEventCenterConfig cfg = (ActiveMQEventCenterConfig) configWrapper.getWrappedInstance();

                center.setRedeliveryPolicy(cfg.getRedeliveryPolicy());
                center.setPrefetchPolicy(cfg.getPrefetchPolicy());


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


}
