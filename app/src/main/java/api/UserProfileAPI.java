package api;

import android.media.Image;

import java.util.List;

import validate.User;

public class UserProfileAPI {
    private String token;
    /*
     * private int id;
     * private int count;
     * protected String email;
     * protected String name;
     * protected int profilePic; // Find image class
     */

    public UserProfileAPI(String token) {
        this.token = token;
    }

    /**
     * Unique identification number for user.
     *
     * @return id
     */
    public int getID() {
        return 0;
    }

    public String getUsername() {
        return "";
    }

    public void setUsername(String username) {
    }

    public List<Integer> getFollowedUsers() {
        return null;
    }

    public void setFollowedUsers(List<Integer> followedUsers) {

    }

    public String getName() {
        return "";
    }

    public void setName(String name) {
        User.checkName(name);
    }

    public Image getProfilePic() {
        return null;
    }

    public void setProfilePic(Image profilePic) {
    }

    public boolean changePassword(String originalPassword, String newPassword1, String newPassword2) {
        return false;
    }

    public boolean deleteUser() {
        return false;
    }
}
