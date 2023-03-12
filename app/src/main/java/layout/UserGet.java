package layout;

public interface UserGet {
    /**
     * Login function for user.
     *
     * @param username Username of user.
     * @param password Password of user.
     *
     * @return String token if user successfully logged in, otherwise empty string.
     */
    OutputPair login(String username, String password);

    /**
     * Add new user to source.
     *
     * @param email Email of user.
     * @param username Username of user.
     * @param password Password of user.
     *
     * @return true if user has been successfully added.
     */
    OutputPair register(String email, String username, String password);

    OutputPair getUserID(String username, String token);
}
