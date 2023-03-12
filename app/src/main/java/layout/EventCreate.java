package layout;

import android.media.Image;

import java.util.Date;

public interface EventCreate {
    /**
     * Create a new event to add to the source.
     *
     * Only adds an event if all fields pass validation and user is an organiser.
     *
     * @param eventType Event category id.
     * @param title Name of event.
     * @param description Description of event.
     * @param datetime Date and time of event.
     * @param location Location of event.
     * @param image Image of event.
     * @param hasRSVP true if RSVP is done through the app.
     *
     * @return If successful, the new event is returned by its id.
     **/
    OutputPair createEvent(
            int eventType, String title, String description, Date datetime, String address1, String address2, String postcode, Image image,
            String organiserToken
    );
}
