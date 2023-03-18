package usertest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;

import api.UserProfile;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;
import validate.UserValidate;
import api.UserGet;
import layout.OutputPair;

public class UserUnitTest {
    public static final String testUsername = "username";
    public static final String testPassword = "password123";

    public static final String testIncorrectPassword = "password";
    public static final String testToken = "5d3f89aea81317f5b26a30aceb4944d33edc18e0";

    public static final int testID = 4;
    public static final String testName = "This user has no name.";
    public static final String testBio = "This user has no description.";

    /**
     * How to login.
     */
    @Test
    public void correctLogin() {
        UserGet userGet = new UserGet();
        OutputPair out = userGet.login(testUsername, testPassword);
        // out looks like (boolean, string message)
        // if successful, the message is the token.
        String token = out.getMessage();
        assertEquals(testToken, token);
    }

    @Test
    public void incorrectLogin() {
        UserGet userGet = new UserGet();
        OutputPair out = userGet.login(testUsername, testIncorrectPassword);
        assertEquals(false, out.isSuccess());
        assertEquals("Incorrect credentials", out.getMessage());
    }

    @Test
    public void emptyLogin() {
        UserGet userGet = new UserGet();
        OutputPair out = userGet.login("", "");
        assertEquals(false, out.isSuccess());
        //assertEquals("Password field is blank.\nUsername field is blank.", out.getMessage());
    }

    @Test
    public void registerUser() {
        String email = "foo@bar.com";
        String username = "username100";
        String password = "newpassword";

        UserGet userGet = new UserGet();
        OutputPair output_register = userGet.register(email, username, password);
        assertTrue(output_register.isSuccess());

        OutputPair output_login = userGet.login(username, password);
        assertTrue(output_login.isSuccess());
        String token = output_login.getMessage();

        OutputPair idSearch = userGet.getUserID(testUsername, token);
        int userID = Integer.parseInt(idSearch.getMessage());

        try {
            UserProfile user = new UserProfile(token, userID);
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    @Test
    public void correctGetUserID() {
        UserGet userGet = new UserGet();
        OutputPair idSearch = userGet.getUserID("username", testToken);
        int idActual = Integer.parseInt(idSearch.getMessage());

        assertEquals(testID, idActual);
    }

    @Test
    public void usernameNotPresentGetUserID() {
        UserGet userGet = new UserGet();
        OutputPair idSearch = userGet.getUserID("usernam", testToken);

        assertEquals(false, idSearch.isSuccess());
        assertEquals("Username not found.", idSearch.getMessage());
    }

    /**
     * How to login and create user profile class.
     */
    @Test
    public void UserProfileFromScratch() {
        try {
            // logging in
            UserGet userGet = new UserGet();
            OutputPair out = userGet.login(testUsername, testPassword);
            String token = out.getMessage();

            // get user id
            OutputPair idSearch = userGet.getUserID(testUsername, token);
            int userID = Integer.parseInt(idSearch.getMessage());

            // create user profile
            UserProfile user = new UserProfile(testToken, userID);
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    @Test
    public void attributesTestOwnUserUserProfileAPI() {
        try {
            UserProfile user = new UserProfile(testToken, testID);
            assertEquals(testUsername, user.getUsername());
            assertEquals(true, user.isOrganiser());
            assertEquals(testName, user.getName());
            assertEquals(testBio, user.getBio());
            assertTrue(new LinkedList<Integer>().equals(user.getFollowedUsers()));
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }
}