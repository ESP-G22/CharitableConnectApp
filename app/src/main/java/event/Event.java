package event;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Event {
    private int id;
    private int type;
    private String title;
    private String description;
    private Date datetime;
    private String location;
    private int image; // need to find suitable image class
    private boolean hasRSVP;
    private int numAttendees;
    private List<Integer> attendees;
    private List<Integer> organisers;

    public Event(
            int id, int type, String title,
            String description, Date datetime,
            String location, int image, boolean hasRSVP, List<Integer> attendees, List<Integer> organisers) {

        setID(id);
        setType(type);
        setTitle(title);
        setDescription(description);
        setDatetime(datetime);
        setLocation(location);
        setImage(image);
        setHasRSVP(hasRSVP);
        for (int userID : attendees) {
            addAttendee(userID);
        }
        setOrganisers(organisers);
    }

    /**
     * Unique identification number for event.
     * 
     * @return id
     */
    public int getID() {
        return id;
    }

    private void setID(int id) {
        this.id = id;
    }

    /**
     * Category of event, as represented by an integer.
     * 
     * @return type
     */
    public int getType() {
        return type;
    }

    private void setType(int type) {
        this.type = type;
    }

    /**
     * Title of event.
     * 
     * @return title
     */
    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        EventValidate.checkTitle(title);
        this.title = title;
    }

    /**
     * Description of event.
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        EventValidate.checkDescription(description);
        this.description = description;
    }

    /**
     * Date and time of event.
     * 
     * @return datetime
     */
    public Date getDatetime() {
        return datetime;
    }

    private void setDatetime(Date datetime) {
        EventValidate.checkDatetime(datetime);
        this.datetime = datetime;
    }

    /**
     * Postcode of event.
     * 
     * @return location
     */
    public String getLocation() {
        return location;
    }

    private void setLocation(String location) {
        EventValidate.checkLocation(location);
        this.location = location;
    }

    /**
     * Image chosen by event organiser to show event.
     * 
     * @return image
     */
    public int getImage() {
        return image;
    }

    private void setImage(int image) {
        this.image = image;
    }

    /**
     * Determines if you can RSVP to the event.
     * True if enabled, False otherwise
     * 
     * @return hasRSVP
     */
    public boolean getHasRSVP() {
        return hasRSVP;
    }

    private void setHasRSVP(boolean hasRSVP) {
        this.hasRSVP = hasRSVP;
    }

    /**
     * Event organisers' IDs.
     * 
     * @return organisers
     */
    public List<Integer> getOrganisers() {
        return organisers;
    }

    private void setOrganisers(List<Integer> organisers) {
        this.organisers = organisers;
    }

    /**
     * Adds user ID to event attendance, using RSVP.
     * 
     * @param userID Integer ID from user.getID().
     */
    public void addAttendee(int userID) {
        attendees.add(userID);
        numAttendees++;
    }

    /**
     * Removes user from attendance, if they are on the attendance list.
     * 
     * @param userID Integer ID from user.getID().
     * 
     * @return True if user removed, False if user not on list.
     */
    public boolean removeAttendee(int userID) {
        if (!attendees.contains(userID)) {
            return false;
        }

        attendees.remove(userID);
        numAttendees--;

        return true;
    }

    public static boolean postNewEvent(int type, String title,
            String description, Date datetime,
            String location, int image, boolean hasRSVP, List<Integer> organisers) {

        // check inputs
        // check if event doesn't already exist
        // post event to db

        return false;
    }

    // TODO: Use API documentation to correctly use API
    public static Event getFromApi() {
        return null;
    }
}
