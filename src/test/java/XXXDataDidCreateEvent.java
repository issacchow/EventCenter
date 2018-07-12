import com.isc.eventCenter.Event;
import com.isc.eventCenter.annotation.EventConfig;

/**
 * Created by IssacChow on 17/6/9.
 */
@EventConfig(eventName = "XXXDataDidCreateEvent")
public class XXXDataDidCreateEvent extends Event {
    private long rentalId;

    public long getRentalId() {
        return rentalId;
    }

    public void setRentalId(long rentalId) {
        this.rentalId = rentalId;
    }


}
