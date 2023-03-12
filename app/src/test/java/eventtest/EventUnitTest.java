package eventtest;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

import api.EventAPI;
import api.UserProfileAPI;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;
import layout.OutputPair;
import layout.UserProfile;
import validate.EventValidate;
import usertest.UserUnitTest;


public class EventUnitTest {
    public static final int testEventType = 0;
    public static final String testEventTitle = "Test";
    public static final String testEventDesc = "This is a description";
    public static final Date testEventDateTime = new Date();
    public static final String testEventLocation = "Bath";

    @Ignore
    public void correctCreateEvent() {
        try {
            UserProfileAPI user = new UserProfileAPI(UserUnitTest.testToken, UserUnitTest.testID);
            OutputPair out = user.createEvent(testEventType, testEventTitle, testEventDesc, testEventDateTime, "", "", "", null);
            //assertTrue(out.isSuccess());
            assertEquals("Event has been created.", out.getMessage());
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    @Test
    public void getEvent() {
        try {
            UserProfileAPI user = new UserProfileAPI(UserUnitTest.testToken, UserUnitTest.testID);
            EventAPI event = new EventAPI(1, user);
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    @Test
    public void daysUntilEvent() {
        try {
            UserProfileAPI user = new UserProfileAPI(UserUnitTest.testToken, UserUnitTest.testID);
            EventAPI event = new EventAPI(1, user);
            System.out.println(event.daysUntilEvent());
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }
    @Ignore
    public void EmptyTitle() {
        assertThrows(
                EmptyInputError.class,
                () -> {
                    EventValidate.checkTitle("");
                });
    }

    @Ignore
    public void ExpectedTitle() {
        EventValidate.checkTitle("ah");
    }

    @Ignore
    public void MaximumTitle() {
        assertThrows(
                MaximumInputSizeError.class,
                () -> {
                    EventValidate.checkTitle(
                            "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                });
    }

    @Ignore
    public void EmptyDescription() {
        assertThrows(
                EmptyInputError.class,
                () -> {
                    EventValidate.checkDescription("");
                });
    }

    @Ignore
    public void ExpectedDescription() {
        EventValidate.checkDescription("ah");
    }

    @Ignore
    public void MaximumDescription() {
        assertThrows(
                MaximumInputSizeError.class,
                () -> {
                    EventValidate.checkDescription(
                            "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                });
    }

    @Ignore
    public void nullDatetime() {
        assertTrue(false);
    }

    @Ignore
    public void FutureDatetime() {
        assertTrue(false);
    }

    @Ignore
    public void locationNotInBA2() {
        assertTrue(false);
    }

    @Ignore
    public void locationInBA2() {
        assertTrue(false);
    }
}

