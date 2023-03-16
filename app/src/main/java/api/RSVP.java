package api;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import layout.OutputPair;
import layout.RSVPAttributes;

public class RSVP implements RSVPAttributes, Parcelable {
    private final int rsvpID;
    private final int eventID;
    private final int userID;
    private String authHeaderValue;

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
        return Util.getRequest(Util.getRSVPEndpoint(getID()), getAuthHeaderValue());
    }

    public static OutputPair create(int userID, int eventID, String authHeaderValue) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("user", userID);
        params.put("event", eventID);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.ENDPOINT_RSVP_CREATE);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            conn.setDoOutput(true);
            Util.passParams(conn, input);
        } catch (IOException err) {
            return new OutputPair(false, Util.PROBLEM_WITH_SENDING_REQUEST);
        }

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the id
        int id;
        try {
            InputStream inputStream = conn.getInputStream();
            JSONArray out = Util.getJSONResponse(inputStream);
            JSONObject json = out.getJSONObject(0).getJSONObject("data");
            id = json.getInt("id");

            return Util.disconnect(conn, new OutputPair(true, Integer.toString(id)));
        } catch (IOException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with getting token"));
        } catch (JSONException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with parsing JSON"));
        }
    }

    public OutputPair delete() {
        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.getRSVPEndpoint(rsvpID));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", getAuthHeaderValue());
            conn.setDoOutput(true);
        } catch (IOException err) {
            return new OutputPair(false, "This user cannot do this operation.");
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
            return Util.disconnect(conn, new OutputPair(true, out.getJSONObject(0).getString("msg")));
        } catch (IOException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with getting token"));
        } catch (JSONException err) {
            return Util.disconnect(conn, new OutputPair(false, "Problem with parsing JSON"));
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
