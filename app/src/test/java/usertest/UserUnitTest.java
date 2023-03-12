package usertest;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import android.icu.util.Output;

import java.util.LinkedList;

import api.UserProfileAPI;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;
import layout.UserProfile;
import validate.UserValidate;
import api.UserGetAPI;
import layout.OutputPair;

public class UserUnitTest {
    public static final String testUsername = "username";
    public static final String testPassword = "password123";
    public static final String testToken = "5d3f89aea81317f5b26a30aceb4944d33edc18e0";

    public static final int testID = 4;
    public static final String testName = "This user has no name.";
    public static final String testBio = "This user has no bio.";

    @Test
    public void correctLogin() {
        UserGetAPI userGet = new UserGetAPI();
        OutputPair out = userGet.login(testUsername, testPassword);
        String token = out.getMessage();
        assertEquals(testToken, token);
    }

    @Test
    public void incorrectLogin() {
        UserGetAPI userGet = new UserGetAPI();
        OutputPair out = userGet.login(testUsername, "password12");
        assertEquals(false, out.isSuccess());
        //assertEquals("Unable to log in with provided credentials.", out.getMessage());
    }

    @Test
    public void emptyLogin() {
        UserGetAPI userGet = new UserGetAPI();
        OutputPair out = userGet.login("", "");
        assertEquals(false, out.isSuccess());
        //assertEquals("Password field is blank.\nUsername field is blank.", out.getMessage());
    }

    @Test
    public void correctGetUserID() {
        UserGetAPI userGet = new UserGetAPI();
        OutputPair idSearch = userGet.getUserID("username", testToken);
        int idActual = Integer.parseInt(idSearch.getMessage());

        assertEquals(testID, idActual);
    }

    @Test
    public void usernameNotPresentGetUserID() {
        UserGetAPI userGet = new UserGetAPI();
        OutputPair idSearch = userGet.getUserID("usernam", testToken);

        assertEquals(false, idSearch.isSuccess());
        assertEquals("Username not found.", idSearch.getMessage());
    }

    @Test
    public void attributesTestOwnUserUserProfileAPI() {
        try {
            UserProfileAPI user = new UserProfileAPI(testToken, 4);
            assertEquals(testUsername, user.getUsername());
            assertEquals(false, user.isOrganiser());
            assertEquals(testName, user.getName());
            assertEquals(testBio, user.getBio());
            assertTrue(new LinkedList<Integer>().equals(user.getFollowedUsers()));
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }
    @Ignore
    public void EmptyEmail() {
        assertThrows(
                EmptyInputError.class,
                () -> {
                    UserValidate.checkEmail("");
                });
    }

    @Ignore
    public void ExpectedEmail() {
        UserValidate.checkEmail("foo@bar.com");
    }

    @Ignore
    public void IncorrectEmail() {
        UserValidate.checkEmail("foobar.com");
    }

    @Ignore
    public void MaximumEmail() {
        assertThrows(
                MaximumInputSizeError.class,
                () -> {
                    UserValidate.checkEmail(
                            "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                });
    }
}