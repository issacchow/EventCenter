import com.isc.eventCenter.Event;

/**
 * Created by IssacChow on 17/6/9.
 */
public class XXXDataDidCreateEvent extends Event {
    private long rentalId;

    public long getRentalId() {
        return rentalId;
    }

    public void setRentalId(long rentalId) {
        this.rentalId = rentalId;
    }

}
