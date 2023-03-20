package layout;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.Date;
import java.util.List;

public interface EventCreateProperties {
    OutputPair createEvent(
            String eventType, String title, String description, Date datetime, String address1, String address2, String postcode, List<Bitmap> images,
            String organiserToken
    );
}
