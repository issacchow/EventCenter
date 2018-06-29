import com.isc.eventCenter.Event;
import com.isc.eventCenter.EventDispatchMode;
import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventListener;
import com.isc.eventCenter.annotation.EventDispatchConfig;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IssacChow on 17/6/18.
 */
public class ActiveMQCenterTest extends TesterBase {

    /**
     * 消息事件并发发布事件测试
     */
    @Test
    public void eventPublishingConncurentTest(){

        int timerCount = 1000;
        int index= 0;


        IEventCenter eventCenter = buildEventCenter("publisher");
        eventCenter.connect();
        BroadcastEvent event = new BroadcastEvent();
        event.eventData = "aaabcc";

        int temp = timerCount;
        while(temp-->0){
            index++;
            Timer timer = new Timer(String.format("timer-%s",index));
            PublishEventTask task = new PublishEventTask();
            task.eventCenter = eventCenter;
            task.willPublishEvent = event;
            task.id = String.format("task-%s",index);
            timer.scheduleAtFixedRate(task,1000,1000);
        }

        System.out.println();
        System.out.printf("%s timers is running", timerCount);
        while (true){

        }

    }

    @Test
    public void eventConsumerTest(){
        IEventCenter eventCenter = buildEventCenter("consumer");
        eventCenter.registerEventListener(new BroadcastEventListener("broadcastEventListener1"));
        eventCenter.connect();
        eventCenter.reloadAllListener();

        System.out.println();
        System.out.print("consumer running");

        while (true){

        }
    }

    @Test
    public void eventConsumerTest2(){
        IEventCenter eventCenter = buildEventCenter("consumer2");
        eventCenter.registerEventListener(new BroadcastEventListener("broadcastEventListener2"));
        eventCenter.connect();
        eventCenter.reloadAllListener();

        System.out.println();
        System.out.print("consumer running");

        while (true){

        }
    }






    @Test
    public void eventCenterFactoryTest(){
       IEventCenter eventCenter = buildEventCenter("xxx");
        eventCenter.connect();
    }





    /** class **/

    private class PublishEventTask extends TimerTask{

        public IEventCenter eventCenter;

        public Event willPublishEvent;

        public String id;

        @Override
        public void run() {
            System.out.printf("\ntask %s run...", id);
            try {
                eventCenter.publishEvent(willPublishEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class BroadcastEventListener implements IEventListener<BroadcastEvent> {

        private String name;
        @Override
        public String getName() {
            return  this.name;
        }

        public BroadcastEventListener(String name){
            this.name = name;
        }

        @Override
        public boolean onEvent(IEventCenter eventCenter, BroadcastEvent event) {

            System.out.println();
            System.out.print("consume a event");
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
