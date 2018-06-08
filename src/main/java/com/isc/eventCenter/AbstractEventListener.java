package com.isc.eventCenter;

/**
 * Created by IssacChow on 18/6/9.
 */
public abstract class AbstractEventListener {
    protected String name;
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = String.format("%s-%s",this.getClass().getSimpleName(),name);
    }

}
