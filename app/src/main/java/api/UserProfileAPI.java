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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import layout.OutputPair;
import layout.UserProfile;
import validate.UserValidate;

public class UserProfileAPI implements UserProfile {
    private final String token;
    private final String authHeaderValue;
    private final int id;
    private String username;
    private String bio;
    private String name;
    private String userType;
    private List<Integer> followedUsers;

    public UserProfileAPI(String token, int id) throws Exception {
        this.token = token;
        this.authHeaderValue = Util.createToken(this.token);
        this.id = id;
        OutputPair out = getAttrs();

        if (!out.isSuccess()) {
            throw new Exception(out.getMessage());
        }

        JSONArray arr = new JSONArray(out.getMessage());
        JSONObject attrs = arr.getJSONObject(0);

        this.username = attrs.getString("user");
        if (attrs.isNull("name")) {
            this.name = "This user has no name.";
        } else {
            this.name = attrs.getString("name");
        }
        if (attrs.isNull("description")) {
            this.bio = "This user has no bio.";
        } else {
            this.bio = attrs.getString("description");
        }
        this.userType = attrs.getString("userType");

        ObjectMapper mapper = new ObjectMapper();
        this.followedUsers = new LinkedList<>();
        this.followedUsers = Arrays.asList(mapper.readValue(attrs.getJSONArray("followedUsers").toString(), Integer[].class));
    }

    private OutputPair getAttrs() {
        // Establish connection and post JSON parameters
        HttpURLConnection conn;
        try {
            URL url = new URL(Util.getUserEndpoint(id));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
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

    public void applyAuthHeaderValue(HttpURLConnection conn) {
        conn.setRequestProperty("Authorization", authHeaderValue);
    }

    public int getID() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        return;
    }

    public List<Integer> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(List<Integer> followedUsers) {
        return;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        UserValidate.checkName(name);
    }

    public String getBio() { return bio; }

    public void setBio(String bio) {

    }

    public String getUserType() { return userType; }

    public boolean isOrganiser() {
        return getUserType().equals("ORGANISER");
    }

    public boolean isAttendee() {
        return getUserType().equals("USER");
    }

    public Image getProfilePic() {
        return null;
    }

    public void setProfilePic(Image profilePic) {
    }

    public OutputPair changePassword(String originalPassword, String newPassword1, String newPassword2) {
        if (!newPassword1.equals(newPassword2)) {
            return new OutputPair(false, "Passwords must match.");
        }
        return new OutputPair(false, "Cannot do this operation");
    }

    public OutputPair deleteUser() {
        return new OutputPair(false, "Cannot do this operation");
    }

    public OutputPair createEvent(int eventType, String title, String description, Date datetime,
                                  String address1, String address2, String postcode, Image image) {
        EventCreateAPI eventCreate = new EventCreateAPI();

        return eventCreate.createEvent(eventType, title, description, datetime, address1, address2, postcode, image, authHeaderValue);
    }

    public EventAPI getEvent(int eventID) throws Exception {
        return new EventAPI(eventID, this);
    }
}
