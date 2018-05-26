package com.isc.eventCenter.impl.activemq;

import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventCentorFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by IssacChow on 18/5/24.
 */
public class ActiveMQEventCenterFactory implements IEventCentorFactory {


    private ResourceLoader resourceLoader = null;

    private ResourceLoader getResourceLoader() {
        if(resourceLoader==null){
            resourceLoader = new PathMatchingResourcePatternResolver();
        }
        return resourceLoader;
    }



    @Override
    public IEventCenter build() {
        ActiveMQEventCenter center = new ActiveMQEventCenter();
        return center;
    }


    @Override
    public IEventCenter build(String resourceName) {
        Resource resource = getResourceLoader().getResource(resourceName);
        return build(resource);
    }


    @Override
    public IEventCenter build(Resource resource) {

        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadProperties(resource);
            return build(properties);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }



    }


    private ActiveMQEventCenter build(Properties properties){
        ActiveMQEventCenter center = new ActiveMQEventCenter();
        BeanWrapper wrapper = new BeanWrapperImpl(center);
        PropertyValues propertyValues = new MutablePropertyValues(properties);
        wrapper.setPropertyValues(propertyValues,false,false);
        return center;
    }


}
