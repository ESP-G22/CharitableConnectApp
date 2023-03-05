package user;

public abstract class User {
    private String token;
    private int id;
    private int count;
    protected String email;
    protected String name;
    protected int profilePic; // Find image class

    public User(String token) {
        setToken(token);
    }

    public User(int id, int count, String email, String name, int profilePic) {
        setAttrs(id, count, email, name, profilePic);
    }

    private void setToken(String token) {
        // check if token is valid
        this.token = token;

        // get attributes from API
        int id = 0;
        int count = 1;
        String email = "foo@bar.com";
        int profilePic = 0;

        setAttrs(id, count, email, name, profilePic);
    }

    private setAttrs(int id, int count, String email, String name, int profilePic) {
        setID(id);
        setCount(count);
        setEmail(email);
        setName(name);
        setProfilePic(profilePic);
    }

    public boolean changePassword(String originalPassword, String newPassword1, String newPassword2) {
        return false;
    }

    public boolean deleteUser() {
        return false;
    }

    /**
     * Unique identification number for user.
     * 
     * @return id
     */
    public int getID() {
        return id;
    }

    private void setID(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    private void setCount(int count) {
        this.count = count;
    }

    public String getEmail(String email) {
        return email;
    }

    private void setEmail(String email) {
        UserValidate.checkEmail(email);
        this.email = email;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        UserValidate.checkName(name);
        this.name = name;
    }

    public int getProfilePic() {
        return profilePic;
    }

    private void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }
}
