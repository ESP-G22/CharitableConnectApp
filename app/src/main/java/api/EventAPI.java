package api;

import android.media.Image;
import layout.Event;
import java.util.Date;

public class EventAPI implements Event {
    public static final String ENDPOINT = "https://api.cc.n0ne1eft.dev/events/";
    private int id;
    /*
     * private int type;
     * private String title;
     * private String description;
     * private Date datetime;
     * private String location;
     * private int image; // need to find suitable image class
     * private boolean hasRSVP;
     * private int numAttendees;
     * private List<Integer> attendees;
     * private List<Integer> organisers;
     */

    public EventAPI(int id) {
        this.id = id;
    }

    /**
     * Unique identification number for event.
     * 
     * @return id
     */
    public int getID() {
        return id;
    }

    /**
     * Category of event, as represented by an integer.
     *
     * @return type
     */
    public int getEventType() {
        return 0;
    }

    public void setEventType(int eventType) {
    }

    /**
     * Title of event.
     * 
     * @return title
     */
    public String getTitle() {
        return "";
    }

    public void setTitle(String title) {
        validate.Event.checkTitle(title);
    }

    /**
     * Description of event.
     * 
     * @return description
     */
    public String getDescription() {
        return "";
    }

    public void setDescription(String description) {
        validate.Event.checkDescription(description);
    }

    /**
     * Date and time of event.
     * 
     * @return datetime
     */
    public Date getDatetime() {
        return null;
    }

    public void setDatetime(Date datetime) {
        validate.Event.checkDatetime(datetime);
    }

    /**
     * Postcode of event.
     * 
     * @return location
     */
    public String getLocation() {
        return "";
    }

    public void setLocation(String location) {
        validate.Event.checkLocation(location);
    }

    /**
     * Image chosen by event organiser to show event.
     * 
     * @return image
     */
    public Image getImage() {
        return null;
    }

    public void setImage(Image image) {
    }

    /**
     * Determines if you can RSVP to the event.
     * True if enabled, False otherwise
     * 
     * @return hasRSVP
     */
    public boolean getHasRSVP() {
        return false;
    }

    public void setHasRSVP(boolean hasRSVP) {
    }

    /**
     * Event organisers' IDs.
     * 
     * @return organisers
     */
    public layout.UserProfile getOrganiser() {
        return null;
    }

    public void setOrganiser(layout.UserProfile organiser) {

    }

    public static int createEvent() {
        return 0;
    }

    public boolean deleteEvent() {
        return false;
    }
}
