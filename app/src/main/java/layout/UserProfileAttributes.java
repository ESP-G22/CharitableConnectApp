package layout;

import android.graphics.Bitmap;
import android.media.Image;
import java.util.List;

public interface UserProfileAttributes {
    /**
     * Unique identification number for user.
     * @return id
     */
    int getID();

    /**
     * Name of user shown in the system. Used to login.
     * @return Username of user.
     */
    String getUsername();

    /**
     * Get firstname of user.
     * @return name.
     */
    String getName();

    /**
     * A short description of the user, used by organisers to advertise themselves.
     * @return bio.
     */
    String getBio();

    /**
     * A profile picture of the user. Shown on profile page.
     * @return profile picture.
     */
    Bitmap getProfilePic();

    /**
     * Get the level of user. 'ADMIN' for admin, 'ORGANISER' for organiser, 'USER' for attendee.
     * @return user type.
     */
     String getUserType();

    /**
     * Is the user an event organiser?
     * @return true if the user is an event organiser.
     */
    boolean isOrganiser();

    /**
     * Is the user an event attendee?
     * @return true if the user is an event attendee.
     */
    boolean isAttendee();

    /**
     * Get all other users that the user currently follows.
     * @return followed users.
     */
    List<Integer> getFollowedUsers();

    /**
     * Get the number of events the user is the organiser for.
     *
     * @return number of events.
     */
    int getEventCount();

    /**
     * Get the number of users who follow the user.
     *
     * @return follower count.
     */
    int getFollowerCount();

    // update attributes
    void setUsername(String username);

    void setName(String name);

    void setBio(String bio);

    void setProfilePic(Bitmap profilePic);
}
