package api;

import android.graphics.Bitmap;
import android.icu.util.Output;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import layout.OutputPair;
import layout.UserProfileAttributes;
import validate.UserValidate;

public class UserProfile implements UserProfileAttributes, Parcelable {
    public static final String DEFAULT_PROFILE_IMAGE_UUID = "37f9f56a-df80-4dd1-ac46-6135e71dc5ca";
    public static final String defaultName = "This user has no name.";
    public static final String defaultDescription = "This user has no description.";
    private final String token;
    private final int id;
    private String username;
    private String bio;
    private String name;
    private String userType;
    private List<Integer> followedUsers;

    private Bitmap avatar;

    private int eventCount;

    private int followerCount;

    /**
     * Create a user object to get information to display on their profile and subscribe to events.
     *
     * @param token Token the user used to login.
     * @param id ID of user logging in.
     *
     * @throws Exception If the attributes cannot be obtained
     */
    public UserProfile(String token, int id) throws Exception {
        this.token = token;
        this.id = id;
        OutputPair out = getAttrs();

        // If there was an error getting the attributes.
        if (!out.isSuccess()) {
            throw new Exception(out.getMessage());
        }

        // Set attributes from profile get request
        JSONArray arr = new JSONArray(out.getMessage());
        JSONObject attrs = arr.getJSONObject(0);

        this.username = attrs.getString("user");
        this.name = Util.setOptionalStringField(attrs, "name", defaultName);
        this.bio = Util.setOptionalStringField(attrs, "description", defaultDescription);
        this.userType = attrs.getString("userType");

        ObjectMapper mapper = new ObjectMapper();
        this.followedUsers = new LinkedList<>(Arrays.asList(mapper.readValue(attrs.getJSONArray("followedUsers").toString(), Integer[].class)));
        this.eventCount = attrs.getInt("eventCount");
        this.followerCount = attrs.getInt("followerCount");

        // Only choose of of these image setters.
        setImageForTesting();
        //setProperImage(attrs);
    }

    /**
     * Get user profile ID endpoint
     *
     * @return URL string
     */
    public String getUserEndpoint() {
        return Util.getUserEndpoint(getID());
    }

    /**
     * Get all attrs from user profile ID endpoint.
     *
     * @return JSON string where the keys are the attribute names and the
     * values are the attribute values.
     */
    private OutputPair getAttrs() {
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(getUserEndpoint(), getAuthHeaderValue());

        return status;
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
    public List<Integer> getFollowedUsers() {
        return followedUsers;
    }

    @Override
    public int getEventCount() {
        return eventCount;
    }

    @Override
    public int getFollowerCount() {
        return followerCount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getBio() { return bio; }

    @Override
    public void setBio(String bio) {
        this.bio = bio;
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
    public Bitmap getProfilePic() {
        return avatar;
    }

    @Override
    public void setProfilePic(Bitmap profilePic) {
        this.avatar = profilePic;
    }

    public OutputPair editProfile(String name, String bio, Bitmap profilePic) {
        HTTPConnection conn = new HTTPConnection();
        String uuid = null;

        /*
        if (profilePic != null) {
            OutputPair status_image = conn.postImage(
                    Util.ENDPOINT_IMAGE_UPLOAD, profilePic, getAuthHeaderValue()
            );

            if (!status_image.isSuccess()) {
                return status_image;
            }
            uuid = status_image.getMessage();
        }
        */
        Map<String, Object> params = new LinkedHashMap<>();
        if (name != null) {
            params.put("name", name);
        }
        if (bio != null) {
            params.put("description", bio);
        }
        if (uuid != null) {
            params.put("avatar", uuid);
        }
        JSONObject input = new JSONObject(params);

        if (params.isEmpty()) {
            return new OutputPair(true, "No details changed");
        }

        OutputPair status = conn.put(getUserEndpoint(), input, getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }

        if (name != null) {
            setName(name);
        }
        if (bio != null) {
            setBio(bio);
        }
        if (uuid != null) {
            try {
                setProfilePic(Util.getImage(uuid, getAuthHeaderValue()));
            } catch (Exception err) {
                return new OutputPair(false, "Could not set image locally");
            }
        }

        return new OutputPair(true, "Profile details updated");
    }

    /**
     * Before committing, set the image getter to this to avoid unit test errors.
     */
    private void setImageForTesting() {
        this.avatar = null;
    }

    /**
     * If you want to test the GUI and its images, use this function instead of setImageForTesting.
     *
     * @param attrs Where the avatar ID is stored.
     * @throws IOException If the image cannot be obtained.
     * @throws JSONException If the avatar key cannot be obtained.
     */
    private void setProperImage(JSONObject attrs) throws IOException, JSONException {
        if (attrs.isNull("avatar")) {
            this.avatar = Util.getImage(DEFAULT_PROFILE_IMAGE_UUID, getAuthHeaderValue());
        } else {
            this.avatar = Util.getImage(attrs.getString("avatar"), getAuthHeaderValue());
        }
    }

    /**
     * Get short details about an organiser such as subscriber count and number of events.
     *
     * @return Neat string for GUI output.
     */
    public String getInfo() {
        int subs = getFollowedUsers().size();
        String subsStr = (subs == 1) ? "1 Sub": Integer.toString(subs) + " Subs";

        int events = getEventCount();
        String eventsStr = (events == 1) ? "1 Event": Integer.toString(events) + " Events";

        return subsStr + "  ·  " + eventsStr;
    }

    public OutputPair delete() {
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.delete(getUserEndpoint(), getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }

        // If successful, output the success to user
        try {
            JSONArray out = new JSONArray(status.getMessage());
            return new OutputPair(true, out.getJSONObject(0).getString("msg"));
        } catch (JSONException err) {
            return new OutputPair(false, "Problem with parsing JSON");
        }
    }

    /**
     * Subscribe to an event, using its ID.
     *
     * The event is saved in the subscribed feed.
     *
     * @param eventID ID of event to subscribe to.
     *
     * @return The response of the request and its success.
     */
    public OutputPair subscribeToEvent(int eventID) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("event", eventID);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.post(Util.ENDPOINT_RSVP_CREATE, input, getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }
        return new OutputPair(true, "RSVP successful.");
    }

    /**
     * Unsubscribe from an event, using the rsvp ID.
     *
     * @param rsvpID ID of your rsvp to the event.
     *
     * @return The response of the request and its success.
     */
    public OutputPair unsubscribeFromEvent(int rsvpID) {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("id", rsvpID);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.delete(Util.getRSVPEndpoint(rsvpID), input, getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }
        return new OutputPair(true, "RSVP removed.");
    }

    public List<Integer> getSubscribedEvents() throws IOException, JSONException {
        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.get(Util.ENDPOINT_RSVP_SUBSCRIBED, getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            throw new IOException(status.getMessage());
        }

        // If successful, output the events in a list
        JSONArray output = new JSONArray(status.getMessage());
        JSONArray rsvps = output.getJSONObject(0).getJSONArray("rsvps");
        LinkedList<Integer> listOfEventIDs = new LinkedList<>();

        for (int i = 0; i < rsvps.length(); i++) {
            try {
                listOfEventIDs.add(rsvps.getJSONObject(i).getInt("event"));
            } catch (Exception err) {
                // Go to the next object to parse
            }
        }
        return listOfEventIDs;
    }

    /**
     * Change the user's password.
     *
     * @param originalPassword What the password is pre-change.
     * @param newPassword1 What the password is post-change.
     * @param newPassword2 Double-entry verification of newPassword1.
     *
     * @return The response of the request and its success.
     */
    public OutputPair changePassword(String originalPassword, String newPassword1, String newPassword2) {
        // Double entry verification.
        if (!newPassword1.equals(newPassword2)) {
            return new OutputPair(false, "Passwords must match.");
        }
        // If the new password is the old password.
        if (newPassword1.equals(originalPassword)) {
            return new OutputPair(false, "New password is the same as the current password!");
        }
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("oldPassword", originalPassword);
        params.put("newPassword", newPassword2);
        JSONObject input = new JSONObject(params);

        // Establish connection and post JSON parameters
        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.put(Util.ENDPOINT_CHANGE_PASSWORD, input, getAuthHeaderValue());
        conn.disconnect();

        if (!status.isSuccess()) {
            return status;
        }

        return new OutputPair(true, "Password changed.");
    }

    public boolean isSubscribedTo(UserProfile user) {
        return getFollowedUsers().contains(user.getID());
    }
    public OutputPair subscribe(UserProfile followedUser) {
        if (followedUser.getID() == getID()) {
            return new OutputPair(false, "You cannot subscribe to yourself!");
        }
        if (isSubscribedTo(followedUser)) {
            return new OutputPair(false, "You have already subscribed to " + followedUser.getUsername());
        }

        OutputPair status = updateFollowedUsers();
        System.out.println(status.getMessage());

        if (status.isSuccess()) {
            followedUsers.add(followedUser.getID());
            followedUser.setFollowerCount(getFollowerCount() + 1);
        }

        return status;
    }

    public OutputPair unsubscribe(UserProfile followedUser) {
        if (!isSubscribedTo(followedUser)) {
            return new OutputPair(
                    false,
                    "Cannot unsubscribe when you have not subscribed to" + followedUser.getUsername()
            );
        }

        OutputPair status = updateFollowedUsers();

        if (status.isSuccess()) {
            int idx = getFollowedUsers().indexOf(followedUser.getID());
            followedUsers.remove(idx);
            followedUser.setFollowerCount(getFollowerCount() - 1);
        }

        return status;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    private OutputPair updateFollowedUsers() {
        // Convert input into JSON
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("followedUsers", followedUsers);
        JSONObject input = new JSONObject(params);

        HTTPConnection conn = new HTTPConnection();
        OutputPair status = conn.put(getUserEndpoint(), input, getAuthHeaderValue());

        return status;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getToken());
        dest.writeInt(getID());
        dest.writeString(getUsername());
        dest.writeString(getBio());
        dest.writeString(getName());
        dest.writeString(getUserType());
        dest.writeList(getFollowedUsers());
        dest.writeInt(getEventCount());
        dest.writeInt(getFollowerCount());
    }

    public static final Parcelable.Creator<UserProfile> CREATOR
            = new Parcelable.Creator<UserProfile>() {
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    private UserProfile(Parcel in) {
        token = in.readString();
        id = in.readInt();
        username = in.readString();
        bio = in.readString();
        name = in.readString();
        userType = in.readString();
        followedUsers = new LinkedList<Integer>();
        in.readList(followedUsers, Integer.class.getClassLoader());
        eventCount = in.readInt();
        followerCount = in.readInt();
    }
}
