package api;

import android.media.Image;
import layout.Event;
import validate.EventValidate;
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

    public int getID() {
        return id;
    }

    public int getEventType() {
        return 0;
    }

    public void setEventType(int eventType) {
    }

    public String getTitle() {
        return "";
    }

    public void setTitle(String title) {
        EventValidate.checkTitle(title);
    }

    public String getDescription() {
        return "";
    }

    public void setDescription(String description) {
        EventValidate.checkDescription(description);
    }

    public Date getDatetime() {
        return null;
    }

    public void setDatetime(Date datetime) {
        EventValidate.checkDatetime(datetime);
    }

    public String getLocation() {
        return "";
    }

    public void setLocation(String location) {
        EventValidate.checkLocation(location);
    }

    public Image getImage() {
        return null;
    }

    public void setImage(Image image) {
    }

    public boolean getHasRSVP() {
        return false;
    }

    public void setHasRSVP(boolean hasRSVP) {
    }

    public int getOrganiser() {
        return 0;
    }

    public void setOrganiser(int organiser) {

    }

    public boolean deleteEvent() {
        return false;
    }
}
