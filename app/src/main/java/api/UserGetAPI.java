package api;

public class UserGetAPI {
    public String login(String username, String password) {
        return "token";
    }

    public boolean register(String email, String username, String password) {
        return false;
    }
}
