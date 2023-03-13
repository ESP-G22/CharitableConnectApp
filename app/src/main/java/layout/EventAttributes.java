package layout;

import android.media.Image;
import java.util.Date;

public interface EventAttributes {
    static final String NO_ADDRESS2 = "N/A";

    // Get event attributes directly through source
    /**
     * Unique identification number for event.
     *
     * @return id
     */
    int getID();

    /**
     * Event organiser.
     *
     * @return organiser ID.
     */
    int getOrganiserID();

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

    String getFullAddress();

    String getAddress1();

    String getAddress2();

    String getPostcode();

    /**
     * Image chosen by event organiser to show event.
     *
     * @return image
     */
    Image getImage();

    int getAttendeeCount();

    // update event attributes through source
    void setOrganiserID(int organiserID);

    void setEventType(int eventType);

    void setTitle(String title);

    void setDescription(String Description);

    void setDatetime(Date datetime);

    void setAddress1(String address1);

    void setAddress2(String address2);

    void setPostcode(String postcode);

    void setImage(Image image);
}
