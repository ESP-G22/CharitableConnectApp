package api;

import android.media.Image;

import java.util.List;

import layout.UserProfile;
import validate.UserValidate;

public class UserProfileAPI implements UserProfile {
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
        UserValidate.checkName(name);
    }

    public String getBio() { return ""; }

    public void setBio(String bio) {

    }

    public int getUserType() { return 0; }

    public boolean isOrganiser() {
        return getUserType() == 1;
    }

    public boolean isAttendee() {
        return getUserType() == 2;
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
