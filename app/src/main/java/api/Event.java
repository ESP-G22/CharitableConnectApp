package api;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import layout.EventAttributes;
import layout.OutputPair;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Allows the app to edit and view event attributes.
 *
 * The eventRequester provides authorization to view the event and does not have
 * to be the organiser of the event.
 */
public class Event implements EventAttributes, Parcelable {
    public static final String DEFAULT_IMAGE_UUID = "3070b7ae-396c-420d-8579-9445c5216bbb";
    // Image saying that the event has no images.
    private int id;
    private UserProfile eventRequester; // user who instantiated this event.
    private String eventType;
    private String title;
    private String description;
    private Date datetime;
    private String address1;
    private String address2;
    private String postcode;

    private RSVP rsvp; // may be null if no rsvp
    private int organiserID;

    private List<Bitmap> images;
    private int numAttendees;

    /**
     * Create an event instance by it's ID and the user who requested the event.
     *
     * @param id ID of event.
     * @param eventRequester User who requested the event.
     * @throws Exception If the response to get the attributes was invalid.
     *  Also could be a JSONException if the parsing of the attributes did not work.
     */
    public Event(int id, UserProfile eventRequester) throws Exception {
        this.id = id;
        setEventRequester(eventRequester);

        OutputPair out = getAttrs();

        if (!out.isSuccess()) {
            throw new Exception(out.getMessage());
        }

        // set attributes
        JSONArray arr = new JSONArray(out.getMessage());
        JSONObject attrs = arr.getJSONObject(0);

        this.datetime = Util.stringToDate(attrs.getString("date"));
        this.organiserID = attrs.getJSONObject("organiser").getInt("id");
        this.description = attrs.getString("description");
        this.eventType = attrs.getString("type");
        this.title = attrs.getString("title");
        this.postcode = attrs.getString("postcode");
        this.address1 = attrs.getString("address1");
        if (attrs.isNull("address2")) {
            this.address2 = NO_ADDRESS2;
        } else {
            this.address2 = attrs.getString("address2");
        }
        this.numAttendees = attrs.getInt("attendeeCount");

        if (attrs.isNull("rsvp")) {
            this.rsvp = null;
        } else {
            JSONObject rsvp = attrs.getJSONObject("rsvp");
            this.rsvp = new RSVP(rsvp.getInt("id"), eventRequester.getAuthHeaderValue());
        }

        // Only choose of of these image setters.
        setImageForTesting();
        //setProperImage(attrs);
    }

    /**
     * Get all attrs from user profile ID endpoint.
     *
     * @return JSON string where the keys are the attribute names and the
     * values are the attribute values.
     */
    private OutputPair getAttrs() {
        HTTPConnection conn = new HTTPConnection();
        OutputPair output = conn.get(Util.getEventEndpoint(getID()), getAuthHeaderValue());
        conn.disconnect();

        return output;
    }

    /**
     * Get RSVP to the event for the user requester.
     *
     * null if the user has not RSVPed.
     *
     * @return RSVP created.
     */
    public RSVP getRsvp() {
        return rsvp;
    }

    /**
     * Unsubscribe the event requester from this event.
     *
     * @return The response of the request and its success.
     */
    public OutputPair removeRsvp() {
        if (userRequesterHasNotRSVPed()) {
            return new OutputPair(false, "User has not RSVPed.");
        }

        OutputPair output = getEventRequester().unsubscribeFromEvent(getRsvp().getID());

        if (output.isSuccess()) {
            // set RSVP for user to empty
            rsvp = null;
        }

        return output;

    }

    /**
     * Has the user requester RSVPed to the event?
     *
     * @return true if the user has not RSVPed.
     */
    public boolean userRequesterHasNotRSVPed() {
        return getRsvp() == null;
    }

    /**
     * The event requester is subscribed to this event.
     *
     * @return The response of the request and its success.
     */
    public OutputPair addRsvp() {
        if (getRsvp() != null) {
            return new OutputPair(false, "User has already RSVPed.");
        }

        OutputPair output = getEventRequester().subscribeToEvent(getID());

        if (output.isSuccess()) {
            HTTPConnection conn = new HTTPConnection();
            OutputPair attrs_status = conn.get(Util.getEventEndpoint(getID()), getEventRequester().getAuthHeaderValue());
            conn.disconnect();

            try {
                JSONArray arr = new JSONArray(attrs_status.getMessage());
                JSONObject attrs = arr.getJSONObject(0);
                JSONObject rsvp = attrs.getJSONObject("rsvp");  // RSVP JSON objecy
                this.rsvp = new RSVP(rsvp.getInt("id"), eventRequester.getAuthHeaderValue());
            } catch (JSONException err) {
                return new OutputPair(true, "RSVP created but cannot get the new RSVP.");
            }

        }

        return output;
    }

    /**
     * Get the user who created the event instance.
     * Used to determine if the user can edit the event.
     *
     * @return user
     */
    public UserProfile getEventRequester() {
        return eventRequester;
    }

    /**
     * Verifies that eventRequester has sent the HTTP requests for this event.
     *
     * @return authorization header.
     */
    public String getAuthHeaderValue() {
        return getEventRequester().getAuthHeaderValue();
    }

    /**
     * Change event requester. Allows you to change it to the organiser to perform extra operations.
     *
     * @param user User to set as eventRequester.
     */
    public void setEventRequester(UserProfile user) {
        this.eventRequester = user;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public void setEventType(String eventType) {
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public Date getDatetime() {
        return datetime;
    }

    @Override
    public void setDatetime(Date datetime) {

    }

    @Override
    public String getFullAddress() {
        if (NO_ADDRESS2.equals(address2)) {
            return address1;
        }
        return address1 + "\n" + address2;
    }

    @Override
    public String getAddress1() {
        return address1;
    }

    @Override
    public String getAddress2() {
        return address2;
    }

    @Override
    public void setAddress1(String address1) {
    }

    @Override
    public void setAddress2(String address2) {
    }

    @Override
    public String getPostcode() {
        return postcode;
    }

    @Override
    public void setPostcode(String postcode) {

    }

    @Override
    public List<Bitmap> getImages() {
        return this.images;
    }

    @Override
    public void setImages(List<Bitmap> images) {
    }

    @Override
    public int getAttendeeCount() {
        return this.numAttendees;
    }

    @Override
    public int getOrganiserID() {
        return organiserID;
    }

    @Override
    public void setOrganiserID(int organiserID) {
    }

    /**
     * If you want to test the GUI and its images, use this function instead of setImageForTesting.
     *
     * @param attrs Where the event IDs are stored.
     * @throws IOException If the image cannot be obtained.
     * @throws JSONException If the images key cannot be obtained.
     */
    private void setProperImage(JSONObject attrs) throws IOException, JSONException {
        this.images = Util.getImages(attrs.getJSONArray("images"), getAuthHeaderValue());

        if (images.isEmpty()) {
            this.images.add(Util.getImage(DEFAULT_IMAGE_UUID, getAuthHeaderValue()));
        }
    }

    /**
     * Before committing, set the image getter to this to avoid unit test errors.
     */
    private void setImageForTesting() {
        this.images = null;
    }

    /**
     * Get the date of the event, and how many days until the event in string format.
     *
     * If the event requester is the organiser, it will show the attendee count too.
     *
     * @return In the form: {date} · In {x} days[ · {y} going]
     */
    public String getShortInfo() {
        String dateStr = Util.dateToPrettyString(getDatetime());

        String status;
        int days = daysUntilEvent();

        if (days < 0) {
            status = "";
        } else if (days == 0) {
            status = " · Today";
        } else if (days == 1) {
            status = " · In " + Integer.toString(days)  + " day";
        } else {
            status = " · In " + Integer.toString(days)  + " days";
        }

        // For the organiser, it shows how many are attending
        if (eventRequesterIsOrganiser()) {
            return dateStr + status + " · " + Integer.toString(getAttendeeCount()) + " going";
        }

        return dateStr + status;
    }

    /**
     * Get the event description, address, and postcode in a string paragraph.
     *
     * @return In form: {description}\n\nADDRESS\n\n{address1}\n{address2}\n{postcode}
     */
    public String getInfo() {
        String addr2 = getAddress2();
        String addr2Txt;

        if ("N/A".equals(addr2)) {
            addr2Txt = "";
        } else {
            addr2Txt = addr2 + "\n";
        }
        return getDescription() + "\n\nADDRESS\n\n" + getAddress1() + "\n" + addr2Txt + getPostcode();
    }

    /**
     * Days until event from today.
     *
     * @return Days until event, if event has happened, negative if event has already happened.
     */
    public int daysUntilEvent() {
        Date today = new Date();
        long diff = datetime.getTime() - today.getTime();

        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * Is the user who searched for this event the organiser?
     * If not, they cannot do certain operations like deleting the event.
     *
     * @return true if the event requester is the organiser, false otherwise.
     */
    public boolean eventRequesterIsOrganiser() {
        return getEventRequester().getID() == getOrganiserID();
    }

    /**
     * Get all RSVPs for this event.
     *
     * @return List of RSVPs.
     * @throws IOException If the response could not be obtained.
     * @throws JSONException For formatting the output.
     */
    public List<RSVP> getRSVPs() throws IOException, JSONException {
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(Util.getEventRSVPEndpoint(id), getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the rsvps in a list
        JSONArray out = new JSONArray(status.getMessage());
        JSONArray rsvps = out.getJSONObject(0).getJSONArray("data");
        LinkedList<RSVP> listOfRSVPs = new LinkedList<>();
        for (int i = 0; i < rsvps.length(); i++) {
            try {
                listOfRSVPs.add(new RSVP(rsvps.getJSONObject(i).getInt("id"), eventRequester.getAuthHeaderValue()));
            } catch (Exception err) {
                // Continue to next object to parse
            }
        }
        return listOfRSVPs;
    }

    /**
     * Delete event from the system, with event requester permission.
     *
     * @return Status message.
     */
    public OutputPair delete() {
        if (!eventRequesterIsOrganiser()) {
            return new OutputPair(false, "Only the organiser can remove events");
        }
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.delete(Util.getEventEndpoint(id), getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }

        // update local variables
        eventRequester.setEventCount(eventRequester.getEventCount() - 1);

        // If successful, output the success to user
        try {
            JSONArray out = new JSONArray(status.getMessage());
            return new OutputPair(true, out.getJSONObject(0).getString("msg"));
        } catch (JSONException err) {
            return new OutputPair(false, "Problem with parsing JSON");
        }
    }

    /**
     * Get all events that contain the search term.
     *
     * @param phraseInTitle Phrase to filter by.
     * @param eventRequester User who requested the events.
     * @return All events that match, may be empty.
     * @throws IOException If the response could not be obtained.
     * @throws JSONException For formatting the output.
     */
    public static List<Event> search(String phraseInTitle, UserProfile eventRequester) throws IOException, JSONException {
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(
                Util.ENDPOINT_EVENT_SEARCH + "?searchTerm=" + phraseInTitle,
                eventRequester.getAuthHeaderValue()
        );
        conn.disconnect();

        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the events in a list
        JSONArray out = new JSONArray(status.getMessage());
        JSONArray events = out.getJSONObject(0).getJSONArray("data");

        return Event.JSONArrayToListOfEvents(events, eventRequester);
    }

    /**
     * Get all events that are within an event category.
     *
     * Returns blank list if no events found for that type.
     *
     * @param eventType Type to filter by.
     * @param eventRequester User who requested the events.
     * @return All events that match, may be empty.
     * @throws IOException If the response could not be obtained.
     * @throws JSONException For formatting the output.
     */
    public static List<Event> getByEventType(String eventType, UserProfile eventRequester) throws IOException, JSONException {
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(
                Util.ENDPOINT_EVENT_LIST + "?type=" + eventType,
                eventRequester.getAuthHeaderValue()
        );
        conn.disconnect();

        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the events in a list
        JSONArray events = new JSONArray(status.getMessage());

        return Event.JSONArrayToListOfEvents(events, eventRequester);
    }

    /**
     * Get all events that are on a specific date.
     *
     * Returns blank list if no events found for that type.
     *
     * @param datetime Date to filter by.
     * @param eventRequester User who requested the events.
     * @return All events that match, may be empty.
     * @throws IOException If the response could not be obtained.
     * @throws JSONException For formatting the output.
     */
    public static List<Event> getByDate(Date datetime, UserProfile eventRequester) throws IOException, JSONException {
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(
                Util.ENDPOINT_EVENT_LIST + "?date=" + Util.dateToString(datetime),
                eventRequester.getAuthHeaderValue()
        );
        conn.disconnect();

        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the events in a list
        JSONArray events = new JSONArray(status.getMessage());

        return Event.JSONArrayToListOfEvents(events, eventRequester);
    }

    /**
     * Get all events, sorted by attendee count in descending order.
     *
     * @param eventRequester User who requested the events.
     * @return All events that match, may be empty.
     * @throws IOException If the response could not be obtained.
     * @throws JSONException For formatting the output.
     */
    public static List<Event> getTrendingEvents(UserProfile eventRequester) throws IOException, JSONException {
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(
                Util.ENDPOINT_EVENT_LIST + "?sort=attendeeCount",
                eventRequester.getAuthHeaderValue()
        );
        conn.disconnect();

        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the events in a list
        JSONArray out = new JSONArray(status.getMessage());

        return Event.JSONArrayToListOfEvents(out, eventRequester);
    }

    /**
     * Gets all currently advertised events.
     *
     * @param eventRequester User who requested the events.
     * @return list of events on the system, may be empty.
     * @throws IOException Response code error.
     * @throws JSONException Error parsing the JSON response.
     */
    public static List<Event> getEventsList(UserProfile eventRequester) throws IOException, JSONException {
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(
                Util.ENDPOINT_EVENT_LIST,
                eventRequester.getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the events in a list
        JSONArray events = new JSONArray(status.getMessage());

        return Event.JSONArrayToListOfEvents(events, eventRequester);
    }

    /**
     * Convert JSON array from response to list of events.
     *
     * Used after retrieving events from lists endpoint.
     *
     * If an error occurs parsing a JSON value, that event is ignored - no error is raised.
     *
     * @param eventsInJSON JSON format of events in an array.
     * @param eventRequester User who requested the events.
     * @return List of events, may be empty if the JSON array is empty.
     */
    private static List<Event> JSONArrayToListOfEvents(JSONArray eventsInJSON, UserProfile eventRequester) {
        LinkedList<Event> listOfEvents = new LinkedList<>();
        for (int i = 0; i < eventsInJSON.length(); i++) {
            try {
                listOfEvents.add(new Event(eventsInJSON.getJSONObject(i).getInt("id"), eventRequester));
            } catch (Exception err) {
                // Go to the next object to parse
            }
        }
        return listOfEvents;
    }

    /**
     * Converts a list of event IDs to events.
     *
     * @param eventIDs IDs of events to retrieve.
     * @param eventRequester User who requested the events.
     * @return List of events, may be empty if the IDs list is empty.
     * @throws Exception If an event object cannot be created from the ID.
     */
    public static List<Event> idsToEvents(List<Integer> eventIDs, UserProfile eventRequester) throws Exception {
        List<Event> output = new LinkedList<>();

        for (int i = 0; i < eventIDs.size(); i++) {
            output.add(new Event(eventIDs.get(i), eventRequester));
        }

        return output;
    }

    /**
     * Get all events that an organiser is currently advertising.
     *
     * @param organiserID ID of user to get events for.
     * @param eventRequester User who requested the events.
     * @return List of events, may be empty if the organiser has no events.
     * @throws IOException If there was an error getting the events
     * @throws JSONException If there was an error parsing the JSON events.
     */
    public static List<Event> getEventsFromOrganiser(int organiserID, UserProfile eventRequester) throws IOException, JSONException{
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(
                Util.ENDPOINT_EVENT_LIST + "?organiser=" + Integer.toString(organiserID),
                eventRequester.getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the events in a list
        JSONArray events = new JSONArray(status.getMessage());

        return Event.JSONArrayToListOfEvents(events, eventRequester);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle b = new Bundle();
        b.putParcelable("eventRequester", eventRequester);
        b.putParcelable("rsvp", rsvp);

        dest.writeInt(getID());
        dest.writeBundle(b);
        dest.writeString(getEventType());
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeSerializable(getDatetime());
        dest.writeString(getAddress1());
        dest.writeString(getAddress2());
        dest.writeString(getPostcode());

        dest.writeInt(getOrganiserID());
        dest.writeInt(getAttendeeCount());
    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel in) {
        id = in.readInt();
        Bundle bundle = in.readBundle();
        eventRequester = (UserProfile) bundle.getParcelable("eventRequester");
        rsvp = (RSVP) bundle.getParcelable("rsvp");
        eventType = in.readString();
        title = in.readString();
        description = in.readString();
        datetime = (Date) in.readSerializable();
        address1 = in.readString();
        address2 = in.readString();
        postcode = in.readString();
        organiserID = in.readInt();
        numAttendees = in.readInt();
    }
}
