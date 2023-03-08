package layout;

public interface UserGet {
    String login(String username, String password);

    boolean register(String email, String username, String password);
}
