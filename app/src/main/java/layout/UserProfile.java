package layout;

import android.media.Image;

import java.util.List;

public interface UserProfile {
    // get attributes directly from source
    int getID();

    String getUsername();

    String getName();

    String getBio();

    Image getProfilePic();

    // 0 for admin, 1 for organiser, 2 for attendee
    int getUserType();

    List<Integer> getFollowedUsers();

    // update attributes
    void setUsername(String username);

    void setName(String name);

    void setBio(String bio);

    void setProfilePic(Image profilePic);

    void setFollowedUsers(List<Integer> followedUsers);

    // change password
    boolean changePassword(String originalPassword, String newPassword1, String newPassword2);

    // delete user from source
    boolean deleteUser();

}
