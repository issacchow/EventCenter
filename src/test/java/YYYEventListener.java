import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventListener;

/**
 * Created by IssacChow on 17/6/7.
 */
public class YYYEventListener implements IEventListener<XXXDataDidCreateEvent> {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean onEvent(IEventCenter eventCenter, XXXDataDidCreateEvent event) {
        return false;
    }
}
