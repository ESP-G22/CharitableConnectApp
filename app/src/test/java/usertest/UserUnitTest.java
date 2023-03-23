package usertest;

import org.junit.After;
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
    public UserProfile testUser;
    public UserGet userGet;
    public static final String testUsername1 = "username";
    public static final String testPassword1 = "password123";
    public static final String testEmail1 = "foo@bar.com";
    public static final String testName1 = "This user has no name.";
    public static final String testBio1 = "This user has no description.";
    public static final String testToken1 = "5d3f89aea81317f5b26a30aceb4944d33edc18e0";

    public static final int testID1 = 4;

    @Before
    public void createUser() throws Exception {
        testUser = new UserProfile(testToken1, testID1);
        userGet = new UserGet();
    }

    /**
     * How to login.
     */
    @Test
    public void correctLogin() {
        OutputPair out = userGet.login(testUsername1, testPassword1);
        // out looks like (boolean, string message)
        // if successful, the message is the token.
        String token = out.getMessage();
        assertEquals(testToken1, token);
    }

    @Test
    public void incorrectLogin() {
        String incorrectPassword = "incorrect";
        assertNotEquals(incorrectPassword, testPassword1);

        OutputPair out = userGet.login(testUsername1, incorrectPassword);
        assertEquals(false, out.isSuccess());
        assertEquals("Incorrect credentials", out.getMessage());
    }

    @Test
    public void emptyLogin() {
        OutputPair out = userGet.login("", "");
        assertEquals(false, out.isSuccess());
        assertEquals("Username is invalid", out.getMessage());
    }

    @Test
    public void registerUser() {
        String email = "foo@bar.com";
        String username = "username100";
        String password = "newpassword";

        OutputPair output_register = userGet.register(email, username, password);

        if (output_register.getMessage().contains("A user with that username already exists")) {
            return;
        }
        assertTrue(output_register.isSuccess());
    }

    @Test
    public void correctGetUserID() {
        OutputPair idSearch = userGet.getUserID(testUsername1, testToken1);
        int idActual = Integer.parseInt(idSearch.getMessage());

        assertEquals(testID1, idActual);
    }

    @Test
    public void usernameNotPresentGetUserID() {
        String incorrectUsername = "usernam";
        OutputPair idSearch = userGet.getUserID(incorrectUsername, testToken1);

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
            OutputPair out = userGet.login(testUsername1, testPassword1);
            String token = out.getMessage();

            // get user id
            OutputPair idSearch = userGet.getUserID(testUsername1, token);
            int userID = Integer.parseInt(idSearch.getMessage());

            // create user profile
            UserProfile user = new UserProfile(testToken1, userID);
            assertEquals(user.getID(), testUser.getID());
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    @Test
    public void attributesTestOwnUserUserProfileAPI() {
        try {
            assertEquals(testUsername1, testUser.getUsername());
            assertEquals(true, testUser.isOrganiser());
            assertEquals(testName1, testUser.getName());
            assertEquals(testBio1, testUser.getBio());
            assertTrue(new LinkedList<Integer>().equals(testUser.getFollowedUsers()));
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    @Test
    public void changeDescription() {
        String newdesc = "I am a movie fanatic!";
        if (newdesc.equals(testUser.getBio())) {
            newdesc = "I am a Quiz master.";
        }
        OutputPair status = testUser.editProfile(null, newdesc, null);

        if (!status.isSuccess()) {
            fail(status.getMessage());
        }

        assertEquals(testUser.getBio(), newdesc);
    }
}