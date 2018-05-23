import com.isc.eventCenter.impl.activemq.ActiveMQEventCenter;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by IssacChow on 17/6/7.
 */
public class EventCenterTest {

    @Test
    public void eventListenTest() throws InterruptedException {

        ActiveMQEventCenter center = new ActiveMQEventCenter();
        center.setId("IEventCenter-00001");

        //两个监听相同事件的监听器
        XXXEventListener listener = new XXXEventListener();
        YYYEventListener listener2 = new YYYEventListener();


        center.registerEventListener(listener);
        center.registerEventListener(listener2);

        center.connect();

        while (true) {
            Thread.sleep(1000);
        }

    }

    @Test
    public void eventPublishTest(){
        ActiveMQEventCenter center = new ActiveMQEventCenter();
        center.setId("IEventCenter-00002");

        XXXDataDidCreateEvent event = new XXXDataDidCreateEvent();
        event.setId(UUID.randomUUID().toString());
        event.setRentalId(1L);

        center.connect();
        try {
            center.publishEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void eventSerializeTest(){

        XXXDataDidCreateEvent event = new XXXDataDidCreateEvent();
        event.setId(UUID.randomUUID().toString());
        event.setRentalId(1L);

        event.getName();
        Gson gson = new Gson();
        String json = gson.toJson(event, XXXDataDidCreateEvent.class);
        System.out.println(json);

        XXXDataDidCreateEvent event2 = gson.fromJson(json, XXXDataDidCreateEvent.class);

    }





}
