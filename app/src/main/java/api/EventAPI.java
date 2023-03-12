package api;

import android.media.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import layout.Event;
import layout.OutputPair;
import layout.UserProfile;
import validate.EventValidate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EventAPI implements Event {
    private int id;
    private UserProfileAPI eventRequester;
    private int eventType;
    private String title;
    private String description;
    private Date datetime;
    private String address1;
    private String address2;
    private String postcode;
    private UserProfileAPI organiser;
    //private int image; // need to find suitable image class
    //private boolean hasRSVP;
    //private int numAttendees;
    //private List<Integer> attendees;

    public EventAPI(int id, UserProfileAPI eventRequester) throws Exception {
        this.id = id;
        this.eventRequester = eventRequester;

        OutputPair out = getAttrs();

        if (!out.isSuccess()) {
            throw new Exception(out.getMessage());
        }

        JSONArray arr = new JSONArray(out.getMessage());
        JSONObject attrs = arr.getJSONObject(0);

        System.out.println(attrs.getString("date"));
        this.datetime = Util.stringToDate(attrs.getString("date"));
        this.organiser = null;
        this.description = attrs.getString("description");
        this.eventType = attrs.getInt("type");
        this.title = attrs.getString("title");
        this.postcode = attrs.getString("postcode");
        this.address1 = attrs.getString("address1");
        if (attrs.isNull("address2")) {
            this.address2 = "N/A";
        } else {
            this.address2 = attrs.getString("address2");
        }
    }

    private OutputPair getAttrs() {
        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.getEventEndpoint(id));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            eventRequester.applyAuthHeaderValue(conn);
            conn.setDoOutput(true);
        } catch (IOException err) {
            return new OutputPair(false, "Problem with sending request");
        }

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the users
        try {
            InputStream inputStream = conn.getInputStream();
            JSONArray out = Util.getJSONResponse(inputStream);
            return Util.disconnect(conn, new OutputPair(true, out.toString()));
        } catch (IOException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with getting token"));
        } catch (JSONException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with parsing JSON"));
        }
    }

    public int getID() {
        return id;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        EventValidate.checkTitle(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        EventValidate.checkDescription(description);
    }

    public Date getDatetime() {
        return datetime;
    }

    public int daysUntilEvent() {
        Date today = new Date();

        long diff = datetime.getTime() - today.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }


    public void setDatetime(Date datetime) {
        EventValidate.checkDatetime(datetime);
    }

    public String getFullAddress() {
        if ("N/A".equals(address2)) {
            return address1;
        }
        return address1 + "\n" + address2;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress1(String address1) {

    }

    public void setAddress2(String address2) {

    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {

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

    public boolean eventRequesterIsOrganiser() {
        return eventRequester.getID() == organiser.getID();
    }

    public boolean deleteEvent() {
        return false;
    }
}
