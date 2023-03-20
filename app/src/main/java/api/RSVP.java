package api;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.util.Map;

import layout.OutputPair;
import layout.RSVPAttributes;

public class RSVP implements RSVPAttributes, Parcelable {
    private final int rsvpID;
    private final int eventID;
    private final int userID;
    private String authHeaderValue;

    /**
     * RSVP for a user to an event.
     *
     * @param rsvpID ID of rsvp.
     * @param authHeaderValue The token of a user.
     * @throws JSONException If the attributes cannot be parsed.
     */
    public RSVP(int rsvpID, String authHeaderValue) throws JSONException {
        this.rsvpID = rsvpID;
        setAuthHeaderValue(authHeaderValue);

        OutputPair out = getAttrs();

        JSONArray arr = new JSONArray(out.getMessage());
        JSONObject attrs = arr.getJSONObject(0).getJSONObject("data");

        this.eventID = attrs.getInt("event");
        this.userID = attrs.getInt("user");
    }

    public String getAuthHeaderValue() {
        return authHeaderValue;
    }

    public void setAuthHeaderValue(String authHeaderValue) {
        this.authHeaderValue = authHeaderValue;
    }

    public int getID() {
        return rsvpID;
    }

    public int getEventID() {
        return eventID;
    }

    public int getUserID() {
        return userID;
    }

    private OutputPair getAttrs() {
        HTTPConnection conn = new HTTPConnection();
        OutputPair output = conn.get(Util.getRSVPEndpoint(getID()), getAuthHeaderValue());
        conn.disconnect();

        return output;
    }

    /**
     * Create an RSVP to an event for a user.
     *
     * @param userID User that wants to rsvp.
     * @param eventID Event to rsvp to.
     * @param authHeaderValue Token of user.
     *
     * @return Status of request. If successful, the RSVP ID is in the message.
     */
    public static OutputPair create(int userID, int eventID, String authHeaderValue) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", userID);
        params.put("event", eventID);
        JSONObject input = new JSONObject(params);

        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.post(Util.ENDPOINT_RSVP_CREATE, input, authHeaderValue);
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the id
        int id;
        try {
            JSONArray out = new JSONArray(status.getMessage());
            JSONObject json = out.getJSONObject(0).getJSONObject("data");
            id = json.getInt("id");

            return new OutputPair(true, Integer.toString(id));
        } catch (JSONException err) {
            return new OutputPair(false, "Problem with parsing JSON");
        }
    }

    /**
     * Remove a RSVP.
     *
     * @return Response.
     */
    public OutputPair delete() {
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.delete(Util.getRSVPEndpoint(rsvpID), getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the users
        try {
            JSONArray out = new JSONArray(status.getMessage());
            return new OutputPair(true, out.getJSONObject(0).getString("msg"));
        } catch (JSONException err) {
            return new OutputPair(false, "Problem with parsing JSON");
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getID());
        dest.writeInt(getEventID());
        dest.writeInt(getUserID());
        dest.writeString(getAuthHeaderValue());
    }

    public static final Parcelable.Creator<RSVP> CREATOR
            = new Parcelable.Creator<RSVP>() {
        public RSVP createFromParcel(Parcel in) {
            return new RSVP(in);
        }

        public RSVP[] newArray(int size) {
            return new RSVP[size];
        }
    };

    private RSVP(Parcel in) {
        rsvpID = in.readInt();
        eventID = in.readInt();
        userID = in.readInt();
        authHeaderValue = in.readString();
    }
}
