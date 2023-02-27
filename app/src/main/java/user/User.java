package user;

public abstract class User {
    private int id;
    private int count;
    protected String email;
    protected String name;
    // protected profilePic
    // protected Map<Integer, Integer> socialLinks;

    public User(int id, int count, String email, String name) {
        setID(id);
        setCount(count);
        setEmail(email);
        setName(name);
    }

    private void setID(int id) {
        this.id = id;
    }

    private void setCount(int count) {
        this.count = count;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return "";
    }

    public boolean deleteUser() {
        return false;
    }

    public User IDToUser(int id) {
        return null;
    }

    public static User getFromApi() {
        return null;
    }
}
