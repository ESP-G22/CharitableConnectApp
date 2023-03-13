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
     * @param organiser User who set up event, must be an organiser.
     *
     * @return If successful, the new event is returned.
     **/
    Event createEvent(
            int eventType, String title, String description, Date datetime,
            String location, Image image, boolean hasRSVP, UserProfile organiser
    );
}
