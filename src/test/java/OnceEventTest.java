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


    @Test
    public void onceEventListener() throws InterruptedException {
        IEventCenter eventCenter = buildEventCenter("onceEventListener1");
        eventCenter.connect();

        OnceEventListener listener = new OnceEventListener("listener1");
        eventCenter.registerEventListener(listener);
        eventCenter.reloadAllListener();

        while(true){
            Thread.sleep(1000);
        }
    }

    @Test
    public void onceEventListener2() throws InterruptedException {
        IEventCenter eventCenter = buildEventCenter("onceEventListener2");
        eventCenter.connect();

        OnceEventListener listener = new OnceEventListener("listener2");
        eventCenter.registerEventListener(listener);
        eventCenter.reloadAllListener();

        while(true){
            Thread.sleep(1000);
        }
    }

    @Test
    public void onceEventExclusiveListener() throws InterruptedException {
        IEventCenter eventCenter = buildEventCenter("onceEventExclusiveListener");
        eventCenter.connect();

        OnceEventExclusiveListener listener = new OnceEventExclusiveListener("exclusiveListener");
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
