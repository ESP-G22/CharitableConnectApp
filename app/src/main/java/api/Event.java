package api;

import android.media.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import layout.EventAttributes;
import layout.OutputPair;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Event implements EventAttributes {
    private int id;
    private UserProfile eventRequester;
    private int eventType;
    private String title;
    private String description;
    private Date datetime;
    private String address1;
    private String address2;
    private String postcode;
    private int organiserID;
    //private int image; // need to find suitable image class
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

        JSONArray arr = new JSONArray(out.getMessage());
        JSONObject attrs = arr.getJSONObject(0);

        this.datetime = Util.stringToDate(attrs.getString("date"));
        this.organiserID = attrs.getJSONObject("organiser").getInt("id");
        this.description = attrs.getString("description");
        this.eventType = attrs.getInt("type");
        this.title = attrs.getString("title");
        this.postcode = attrs.getString("postcode");
        this.address1 = attrs.getString("address1");
        if (attrs.isNull("address2")) {
            this.address2 = NO_ADDRESS2;
        } else {
            this.address2 = attrs.getString("address2");
        }
        this.numAttendees = attrs.getInt("attendeeCount");
    }

    private OutputPair getAttrs() {
        return Util.getRequest(Util.getEventEndpoint(getID()), getAuthHeaderValue());
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
    public int getEventType() {
        return eventType;
    }

    @Override
    public void setEventType(int eventType) {
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
    public Image getImage() {
        return null;
    }

    @Override
    public void setImage(Image image) {
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
     * Days until event from today.
     *
     * @return Days until event, if event has happened, negative if event has already happened.
     */
    public int daysUntilEvent() {
        Date today = new Date();

        long diff = datetime.getTime() - today.getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return days;
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

    public List<RSVP> getRSVPs() throws MalformedURLException, IOException, JSONException {
        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        URL url = new URL(Util.getEventRSVPEndpoint(id));
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", getAuthHeaderValue());
        conn.setDoOutput(true);

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the rsvps in a list
        try {
            InputStream inputStream = conn.getInputStream();
            JSONArray out = Util.getJSONResponse(inputStream);
            JSONArray rsvps = out.getJSONObject(0).getJSONArray("data");
            LinkedList<RSVP> listOfRSVPs = new LinkedList<>();
            for (int i = 0; i < rsvps.length(); i++) {
                try {
                    listOfRSVPs.add(new RSVP(rsvps.getJSONObject(i).getInt("id"), eventRequester.getAuthHeaderValue()));
                } catch (Exception err) {
                    return listOfRSVPs;
                }
            }
            return listOfRSVPs;
        } catch (IOException err) {
            return new LinkedList<>();
        } catch (JSONException err) {
            return new LinkedList<>();
        }
    }

    /**
     * Delete event from the system, with event requester permission.
     *
     * @return Status message.
     */
    public OutputPair delete() {
        if (!eventRequesterIsOrganiser()) {
            return new OutputPair(false, "Only the organiser can remove events.");
        }
        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.getUserEndpoint(id));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", getAuthHeaderValue());
            conn.setDoOutput(true);
        } catch (IOException err) {
            return new OutputPair(false, Util.PROBLEM_WITH_SENDING_REQUEST);
        }

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the success to user
        try {
            InputStream inputStream = conn.getInputStream();
            JSONArray out = Util.getJSONResponse(inputStream);
            return Util.disconnect(conn, new OutputPair(true, out.getJSONObject(0).getString("msg")));
        } catch (IOException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with getting token"));
        } catch (JSONException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with parsing JSON"));
        }
    }

    /**
     * Get all events that contain the search term.
     *
     * @param phraseInTitle Phrase to filter by.
     * @param eventRequester User who requested the events.
     * @return All events that match, may be empty.
     */
    public static List<Event> search(String phraseInTitle, UserProfile eventRequester) {
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.ENDPOINT_EVENT_SEARCH + "?searchTerm=" + phraseInTitle);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", eventRequester.getAuthHeaderValue());
            conn.setDoOutput(true);
        } catch (IOException err) {
            return new LinkedList<>();
        }

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            return new LinkedList<>();
        }

        // If successful, output the events in a list
        try {
            InputStream inputStream = conn.getInputStream();
            JSONArray out = Util.getJSONResponse(inputStream);
            JSONArray events = out.getJSONObject(0).getJSONArray("data");
            LinkedList<Event> listOfEvents = new LinkedList<>();
            for (int i = 0; i < events.length(); i++) {
                try {
                    listOfEvents.add(new Event(events.getJSONObject(i).getInt("id"), eventRequester));
                } catch (Exception err) {
                    return listOfEvents;
                }
            }
            return listOfEvents;
        } catch (IOException err) {
            return new LinkedList<>();
        } catch (JSONException err) {
            return new LinkedList<>();
        }
    }

    /**
     * Gets all currently advertised events.
     *
     * @param eventRequester User who requested the events.
     * @return list of events on the system, may be empty.
     * @throws MalformedURLException If URL is invalid.
     * @throws IOException Response code error.
     * @throws JSONException Error parsing the JSON response.
     */
    public static List<Event> getEventsList(UserProfile eventRequester) throws MalformedURLException, IOException, JSONException {
        HttpURLConnection conn;
        URL url = new URL(Util.ENDPOINT_EVENT_LIST);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", eventRequester.getAuthHeaderValue());
        conn.setDoOutput(true);

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the events in a list
        InputStream inputStream = conn.getInputStream();
        JSONArray out = Util.getJSONResponse(inputStream);
        JSONArray events = out.getJSONObject(0).getJSONArray("data");
        LinkedList<Event> listOfEvents = new LinkedList<>();
        for (int i = 0; i < out.length(); i++) {
            try {
                listOfEvents.add(new Event(events.getJSONObject(i).getInt("id"), eventRequester));
            } catch (Exception err) {
                return listOfEvents;
            }
        }
        return listOfEvents;
    }
}
