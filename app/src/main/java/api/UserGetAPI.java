package api;

import layout.UserGet;

public class UserGetAPI implements UserGet {
    public String login(String username, String password) {
        return "token";
    }

    public boolean register(String email, String username, String password) {
        return false;
    }
}
