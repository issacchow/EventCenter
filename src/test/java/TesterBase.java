import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.impl.activemq.ActiveMQEventCenter;
import com.isc.eventCenter.impl.activemq.ActiveMQEventCenterFactory;

/**
 * Created by IssacChow on 18/6/7.
 */
public abstract class TesterBase {

    protected IEventCenter buildEventCenter(String id){
        ActiveMQEventCenterFactory factory = new ActiveMQEventCenterFactory();
        ActiveMQEventCenter center = (ActiveMQEventCenter) factory.build("classpath:activeMQEventCenter.properties");
        center.setId(id);
        return center;
    }

}
