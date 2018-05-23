import com.isc.eventCenter.Event;
import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventListener;

/**
 * Created by IssacChow on 17/6/7.
 */
public class XXXEventListener implements IEventListener<XXXDataDidCreateEvent> {

    @Override
    public boolean onExecuteEvent(IEventCenter eventCenter, Event event) {
        return false;
    }
}
