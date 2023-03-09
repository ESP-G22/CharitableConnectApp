package layout;

import android.media.Image;
import java.util.Date;

public interface Event {
    // Get event attributes directly through source
    /**
     * Unique identification number for event.
     *
     * @return id
     */
    int getID();

    /**
     * Event organiser's IDs.
     *
     * @return organisers
     */
    int getOrganiser();

    /**
     * Category of event, as represented by an integer.
     *
     * @return type
     */
    int getEventType();

    /**
     * Title of event.
     *
     * @return title
     */
    String getTitle();

    /**
     * Description of event.
     *
     * @return description
     */
    String getDescription();

    /**
     * Date and time of event.
     *
     * @return datetime
     */
    Date getDatetime();

    /**
     * Postcode of event.
     *
     * @return location
     */
    String getLocation();

    /**
     * Image chosen by event organiser to show event.
     *
     * @return image
     */
    Image getImage();

    /**
     * Determines if you can RSVP to the event.
     * True if enabled, False otherwise
     *
     * @return hasRSVP
     */
    boolean getHasRSVP();

    // update event attributes through source
    void setOrganiser(int organiser);

    void setEventType(int eventType);

    void setTitle(String title);

    void setDescription(String Description);

    void setDatetime(Date datetime);

    void setLocation(String location);

    void setImage(Image image);

    void setHasRSVP(boolean hasRSVP);

    // delete event

    /**
     * Delete an event from the source.
     *
     * Only deletes an event if it is organiser's doing.
     *
     * @return true if event successfully removed.
     */
    boolean deleteEvent();
}
