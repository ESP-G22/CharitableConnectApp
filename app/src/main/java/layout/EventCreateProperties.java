package layout;

import android.media.Image;

import java.util.Date;

public interface EventCreateProperties {
    OutputPair createEvent(
            int eventType, String title, String description, Date datetime, String address1, String address2, String postcode, Image image,
            String organiserToken
    );
}
