import com.isc.eventCenter.Event;
import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventListener;
import com.isc.eventCenter.impl.activemq.ActiveMQEventCenter;
import com.isc.eventCenter.impl.activemq.ActiveMQEventCenterFactory;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IssacChow on 17/6/18.
 */
public class ActiveMQCenterTest {

    /**
     * 消息事件并发发布事件测试
     */
    @Test
    public void eventPublishingConncurentTest(){

        int timerCount = 1000;
        int index= 0;


        IEventCenter eventCenter = buildEventCenter("publisher");
        eventCenter.connect();
        TargetEvent event = new TargetEvent();
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
        eventCenter.registerEventListener(new TargetEventListener());
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
        eventCenter.registerEventListener(new TargetEventListener());
        eventCenter.connect();
        eventCenter.reloadAllListener();

        System.out.println();
        System.out.print("consumer running");

        while (true){

        }
    }



    private IEventCenter buildEventCenter(String id){
        ActiveMQEventCenterFactory factory = new ActiveMQEventCenterFactory();
        ActiveMQEventCenter center = (ActiveMQEventCenter) factory.build("classpath:activeMQEventCenter.properties");
        center.setId(id);
        return center;
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

    private class TargetEventListener implements IEventListener<TargetEvent> {

        @Override
        public boolean onExecuteEvent(IEventCenter eventCenter, Event event) {

            System.out.println();
            System.out.print("consume a event");
            return true;
        }
    }

    private class TargetEvent extends Event{
        private String eventData;

        public String getEventData() {
            return eventData;
        }

        public void setEventData(String eventData) {
            this.eventData = eventData;
        }
    }

}
