package api;

import android.media.Image;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import layout.OutputPair;
import layout.UserProfileAttributes;
import validate.UserValidate;

public class UserProfile implements UserProfileAttributes {
    public static final String defaultName = "This user has no name.";
    public static final String defaultDescription = "This user has no description.";
    private final String token;
    private final int id;
    private String username;
    private String bio;
    private String name;
    private String userType;
    private List<Integer> followedUsers;

    public UserProfile(String token, int id) throws Exception {
        this.token = token;
        this.id = id;
        OutputPair out = getAttrs();

        // If there was an error getting the attributes.
        if (!out.isSuccess()) {
            throw new Exception(out.getMessage());
        }

        // Set attributes
        JSONArray arr = new JSONArray(out.getMessage());
        JSONObject attrs = arr.getJSONObject(0);

        this.username = attrs.getString("user");
        this.name = Util.setOptionalStringField(attrs, "name", defaultName);
        this.bio = Util.setOptionalStringField(attrs, "description", defaultDescription);
        this.userType = attrs.getString("userType");

        ObjectMapper mapper = new ObjectMapper();
        this.followedUsers = new LinkedList<>();
        this.followedUsers = Arrays.asList(mapper.readValue(attrs.getJSONArray("followedUsers").toString(), Integer[].class));
    }

    private OutputPair getAttrs() {
        return Util.getRequest(Util.getUserEndpoint(getID()), getAuthHeaderValue());
    }

    /**
     * Token used to authenticate user interactions.
     *
     * @return token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Get string value used in authorization header.
     *
     * @return Token in header.
     */
    public String getAuthHeaderValue() {
        return Util.createToken(getToken());
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
    }

    @Override
    public List<Integer> getFollowedUsers() {
        return followedUsers;
    }

    @Override
    public void setFollowedUsers(List<Integer> followedUsers) {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        UserValidate.checkName(name);
    }

    @Override
    public String getBio() { return bio; }

    @Override
    public void setBio(String bio) {
    }

    @Override
    public String getUserType() { return userType; }

    @Override
    public boolean isOrganiser() {
        return getUserType().equals("ORGANISER");
    }

    @Override
    public boolean isAttendee() {
        return getUserType().equals("USER");
    }

    @Override
    public Image getProfilePic() {
        return null;
    }

    @Override
    public void setProfilePic(Image profilePic) {
    }

    public OutputPair subscribeToEvent(int eventID) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
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
            conn.setRequestProperty("Authorization", getAuthHeaderValue());
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
        return Util.disconnect(conn, new OutputPair(true, "RSVP successful."));
    }

    public OutputPair unsubscribeFromEvent(int rsvpID) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("id", rsvpID);
        JSONObject input = new JSONObject(params);

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
            Util.passParams(conn, input);
        } catch (IOException err) {
            return new OutputPair(false, Util.PROBLEM_WITH_SENDING_REQUEST);
        }

        // Evaluate response code
        OutputPair status = Util.checkResponseCode(conn);
        if (!status.isSuccess()) {
            return status;
        }
        return Util.disconnect(conn, new OutputPair(true, "RSVP removed."));
    }

    public OutputPair changePassword(String originalPassword, String newPassword1, String newPassword2) {
        if (!newPassword1.equals(newPassword2)) {
            return new OutputPair(false, "Passwords must match.");
        }
        if (newPassword1.equals(originalPassword)) {
            return new OutputPair(false, "New password is the same as the current password!");
        }
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("oldPassword", originalPassword);
        params.put("newPassword", newPassword2);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.ENDPOINT_CHANGE_PASSWORD);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", getAuthHeaderValue());
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

        return Util.disconnect(conn, new OutputPair(true, "Password changed."));
    }
}
