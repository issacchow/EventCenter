import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventListener;

/**
 * Created by IssacChow on 17/6/7.
 */
public class XXXEventListener implements IEventListener<XXXDataDidCreateEvent> {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean onExecuteEvent(IEventCenter eventCenter, XXXDataDidCreateEvent event) {
        return false;
    }
}
