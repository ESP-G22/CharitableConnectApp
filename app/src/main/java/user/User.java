package user;

public abstract class User {
    private int id;
    private int count;
    protected String email;
    protected String name;
    protected int profilePic; // Find image class

    public User2(String token) {
        
    }

    public User(int id, int count, String email, String name) {
        setID(id);
        setCount(count);
        setEmail(email);
        setName(name);
        setProfilePic(profilePic);
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

    public String getData() {
        return "";
    }

    public boolean deleteUser() {
        return false;
    }

    public boolean register(String username, String password, String email) {
        return false;
    }

    public boolean login(String username, String password) {
        return false;
    }
}
