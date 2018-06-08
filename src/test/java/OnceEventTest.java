import com.isc.eventCenter.Event;
import com.isc.eventCenter.EventDispatchMode;
import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventListener;
import com.isc.eventCenter.annotation.EventDispatchConfig;
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
        eventCenter.publishEvent(onceEvent);
    }


    @Test
    public void onceEventListener() throws InterruptedException {
        IEventCenter eventCenter = buildEventCenter("onceEventPublisher");
        OnceEvent onceEvent = new OnceEvent();
        eventCenter.connect();

        OnceEventListener listener = new OnceEventListener();
        eventCenter.registerEventListener(listener);
        eventCenter.reloadAllListener();

        while(true){
            Thread.sleep(1000);
        }
    }









    private class OnceEventListener implements IEventListener<OnceEvent> {

        @Override
        public boolean onExecuteEvent(IEventCenter eventCenter, Event event) {

            System.out.println();
            System.out.print("consume a event");
            return true;
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
