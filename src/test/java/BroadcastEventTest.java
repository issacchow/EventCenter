import com.isc.eventCenter.*;
import com.isc.eventCenter.annotation.EventDispatchConfig;
import org.junit.Test;

/**
 * 广播事件测试
 */
public class BroadcastEventTest extends TesterBase {


    @Test
    public void publishBroadcastEvent() throws Exception {
        IEventCenter eventCenter = buildEventCenter("broadcastPublisher");
        BroadcastEvent onceEvent = new BroadcastEvent();
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
    public void broadEventListener() throws InterruptedException {
        IEventCenter eventCenter = buildEventCenter("broadcastEventListener2");
        eventCenter.connect();

        BroadcastEventListener listener = new BroadcastEventListener("listener1");
        BroadcastEventListener listener2 = new BroadcastEventListener("listener2");
        eventCenter.registerEventListener(listener);
        eventCenter.registerEventListener(listener2);
        eventCenter.reloadAllListener();

        while(true){
            Thread.sleep(1000);
        }
    }


    private class BroadcastEventListener extends AbstractEventListener implements IEventListener<BroadcastEvent> {

        public BroadcastEventListener(String name){
            this.setName(name);
        }



        @Override
        public boolean onEvent(IEventCenter eventCenter, BroadcastEvent event) {

            System.out.println();
            System.out.print(String.format("%s -- consume a event,id:%s",this.name,event.getId()));
            return true;
        }
    }



    @EventDispatchConfig(mode = EventDispatchMode.Broadcast)
    private class BroadcastEvent extends Event{
        private String eventData;

        public String getEventData() {
            return eventData;
        }

        public void setEventData(String eventData) {
            this.eventData = eventData;
        }
    }
}
