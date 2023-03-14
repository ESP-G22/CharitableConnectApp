package eventtest;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import api.Event;
import api.EventCreate;
import api.RSVP;
import api.UserProfile;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;
import layout.OutputPair;
import validate.EventValidate;
import usertest.UserUnitTest;


public class EventUnitTest {
    public static final int testEventID = 3;
    public static final int testEventType = 0;
    public static final String testEventTitle = "Test Event";
    public static final String testEventDesc = "This is a description.";
    public static final Date testEventDateTime = new Date();
    public static final String testEventAddress1 = "Computer Science";
    public static final String testEventAddress2 = null;
    public static final String testEventPostcode = "BA2 7AY";

    @Ignore
    public void correctCreateEvent() {
        try {
            UserProfile user = new UserProfile(UserUnitTest.testToken, UserUnitTest.testID);
            EventCreate eventCreate = new EventCreate();
            OutputPair out = eventCreate.createEvent(testEventType, testEventTitle, testEventDesc,
                    testEventDateTime, testEventAddress1, testEventAddress2,
                    testEventPostcode, null, user.getAuthHeaderValue());
            assertTrue(out.isSuccess());
            assertEquals("Event has been created.", out.getMessage());
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    /**
     * How to get an event by its id.
     */
    @Test
    public void getEvent() {
        try {
            UserProfile user = new UserProfile(UserUnitTest.testToken, UserUnitTest.testID);
            Event event = new Event(testEventID, user);
            assertEquals(testEventTitle, event.getTitle());
            assertEquals(testEventDesc, event.getDescription());
            assertEquals(true, event.eventRequesterIsOrganiser());
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    @Test
    public void daysUntilEvent() {
        try {
            UserProfile user = new UserProfile(UserUnitTest.testToken, UserUnitTest.testID);
            Event event = new Event(testEventID, user);
            System.out.println(event.daysUntilEvent());
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    /**
     * How to get an event by a search term in the title.
     */
    @Test
    public void searchEvent() {
        try {
            UserProfile user = new UserProfile(UserUnitTest.testToken, UserUnitTest.testID);
            List<Event> output = Event.search("Test", user);
            assertEquals(testEventID, output.get(0).getID());
        } catch (Exception err) {
            fail(err.getMessage());
        }
    }

    @Ignore
    public void deleteRSVP() {
        try {
            // get user
            UserProfile user = new UserProfile(UserUnitTest.testToken, UserUnitTest.testID);
            // get rsvp
            RSVP r = new RSVP(2, user.getAuthHeaderValue());
            // delete rsvp
            OutputPair out = r.delete();
            System.out.println(out.getMessage());
            assertTrue(out.isSuccess());
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

    @Ignore
    public void createRSVP() {
        try {
            // get user
            UserProfile user = new UserProfile(UserUnitTest.testToken, UserUnitTest.testID);
            // create rsvp
            OutputPair out = RSVP.create(user.getID(), testEventID, user.getAuthHeaderValue());

            assertTrue(out.isSuccess());
            int rsvpID = Integer.parseInt(out.getMessage());
            RSVP r = new RSVP(rsvpID, user.getAuthHeaderValue());
            assertEquals(testEventID, r.getEventID());
            assertEquals(UserUnitTest.testID, r.getUserID());
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

    @Test
    public void getEventRSVPs() {
        try {
            UserProfile user = new UserProfile(UserUnitTest.testToken, UserUnitTest.testID);
            Event e = new Event(3, user);
            assertEquals(UserUnitTest.testID, e.getRSVPs().get(0).getUserID());
        } catch (Exception err) {
            err.printStackTrace();
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

