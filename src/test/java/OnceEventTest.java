import com.isc.eventCenter.Event;
import com.isc.eventCenter.EventDispatchMode;
import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventListener;
import com.isc.eventCenter.annotation.EventDispatchConfig;
import com.isc.eventCenter.impl.activemq.annotation.ExclusiveListener;
import org.junit.Test;

/**
 * Created by IssacChow on 18/6/7.
 */
public class OnceEventTest extends TesterBase {


    @Test
    public void publishOnceEvent() throws Exception {
        IEventCenter eventCenter = buildEventCenter("onceEventPublisher");
        OnceEvent onceEvent = new OnceEvent();
        eventCenter.connect();

        int id = 0;
        while(id<100) {
            id++;
            onceEvent.setId(String.valueOf(id));
            eventCenter.publishEvent(onceEvent);
        }
    }


    //多个消费者均衡消费
    @Test
    public void onceEventListener() throws InterruptedException {
        IEventCenter eventCenter = buildEventCenter("onceEventListener1");
        eventCenter.connect();

        OnceEventListener listener = new OnceEventListener("listener1");
        OnceEventListener listener2 = new OnceEventListener("listener2");
        eventCenter.registerEventListener(listener);
        eventCenter.registerEventListener(listener2);
        eventCenter.reloadAllListener();

        while(true){
            Thread.sleep(1000);
        }
    }


    //独占消费者测试,同时只有一个消费者消费,多个独照消费者监听，除第一个独占消费者其他消费者将会作为后备
    @Test
    public void onceEventExclusiveListener() throws InterruptedException {
        IEventCenter eventCenter = buildEventCenter("onceEventExclusiveListener");
        eventCenter.connect();

        OnceEventExclusiveListener listener = new OnceEventExclusiveListener("exclusiveListener");
        OnceEventExclusiveListener listener2 = new OnceEventExclusiveListener("exclusiveListener2");
        eventCenter.registerEventListener(listener);
        eventCenter.registerEventListener(listener2);
        eventCenter.reloadAllListener();

        while(true){
            Thread.sleep(1000);
        }
    }


    @Test
    public void onceEventRejectListener() throws InterruptedException {
        IEventCenter eventCenter = buildEventCenter("onceEventRejectListener");
        eventCenter.connect();

        OnceEventRejectListener listener = new OnceEventRejectListener("rejectListener");
        eventCenter.registerEventListener(listener);
        eventCenter.reloadAllListener();

        while(true){
            Thread.sleep(1000);
        }
    }













    private class OnceEventListener implements IEventListener<OnceEvent> {

        private String name;
        public OnceEventListener(String name){
            this.name = name;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean onExecuteEvent(IEventCenter eventCenter, Event event) {

            System.out.println();
            System.out.print(String.format("%s -- consume a event,id:%s",this.name,event.getId()));
            return true;
        }
    }

    @ExclusiveListener
    private  class OnceEventExclusiveListener extends OnceEventListener implements IEventListener<OnceEvent>{
        public OnceEventExclusiveListener(String name){
            super(name);
        }
    }

    //事件拒绝监听器
    private class OnceEventRejectListener implements IEventListener<OnceEvent>{

        private String name;
        public OnceEventRejectListener(String name){
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean onExecuteEvent(IEventCenter eventCenter, Event event) {
            System.out.println();
            System.out.print(String.format("%s -- consume a event,id:%s",this.name,event.getId()));
            return false;
        }
    }

    @EventDispatchConfig(mode = EventDispatchMode.Once)
    private class OnceEvent extends Event{
        private String eventData;

        public String getEventData() {
            return eventData;
        }

        public void setEventData(String eventData) {
            this.eventData = eventData;
        }
    }
}
