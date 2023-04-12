package layout;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.File;
import java.util.Date;
import java.util.List;

public interface EventCreateProperties {
    /**
     * Create a new event.
     *
     * @param eventType Event category. Error if an invalid category is passed.
     * @param title Title of event.
     * @param description Description of event.
     * @param datetime Date and time of event.
     * @param address1 First line of address of event.
     * @param address2 Second line of address of event (optional).
     * @param postcode Postcode of event.
     * @param images List of images of event.
     * @param organiserAuthHeaderValue Organiser header value.
     * @return Response of request.
     */
    OutputPair createEvent(
            String eventType, String title, String description, Date datetime, String address1, String address2, String postcode, List<Bitmap> images,
            String organiserAuthHeaderValue
    );
}
