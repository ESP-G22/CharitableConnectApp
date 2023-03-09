package layout;

import android.media.Image;

import java.util.List;

public interface UserProfile {
    // get attributes directly from source

    /**
     * Unique identification number for user.
     *
     * @return id
     */
    int getID();

    /**
     * Name of user shown in the system. Used to login.
     *
     * @return Username of user.
     */
    String getUsername();

    /**
     * Get firstname of user.
     *
     * @return name.
     */
    String getName();

    /**
     * A short description of the user, used by organisers to advertise themselves.
     *
     * @return bio.
     */
    String getBio();

    /**
     * A profile picture of the user. Shown on profile page.
     *
     * @return profile picture.
     */
    Image getProfilePic();

    /**
     * Get the level of user.
     *
     * 0 for admin, 1 for organiser, 2 for attendee
     *
     * @return id of user type.
     */
    int getUserType();

    /**
     * Is the user an event organiser?
     *
     * @return true if the user is an event organiser.
     */
    boolean isOrganiser();

    /**
     * Is the user an event attendee?
     *
     * @return true if the user is an event attendee.
     */
    boolean isAttendee();

    List<Integer> getFollowedUsers();

    // update attributes
    void setUsername(String username);

    void setName(String name);

    void setBio(String bio);

    void setProfilePic(Image profilePic);

    void setFollowedUsers(List<Integer> followedUsers);

    // change password

    /**
     * Change password of the user.
     *
     * @param originalPassword The password that the user wants to change.
     * @param newPassword1 New password.
     * @param newPassword2 Copy new password for double-entry verification.
     *
     * @return true if the change of password was successful.
     */
    boolean changePassword(String originalPassword, String newPassword1, String newPassword2);

    // delete user from source

    /**
     * Delete a user from the source.
     *
     * @return true if the user was deleted successfully.
     */
    boolean deleteUser();

}
