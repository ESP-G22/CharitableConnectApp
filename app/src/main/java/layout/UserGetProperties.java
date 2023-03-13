package layout;

public interface UserGetProperties {
    /**
     * Login function for user.
     *
     * @param username Username of user.
     * @param password Password of user.
     *
     * @return String token if user successfully logged in, otherwise error message.
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

    /**
     * Get user ID by username and token they logged in with. Used to create UserProfile object.
     *
     * @param username Any username in system.
     * @param token Token of user.
     *
     * @return Error message, especially if the username does not exist or the token is wrong.
     */
    OutputPair getUserID(String username, String token);
}
