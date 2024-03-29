package eventtest;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import api.Event;
import api.EventCreate;
import api.RSVP;
import api.UserProfile;
import api.Util;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;
import layout.OutputPair;
import validate.EventValidate;
import usertest.UserUnitTest;


public class EventUnitTest {
    public static final int testEventID = 175;
    public static final String testEventType1 = "Other";
    public static final String testEventTitle1 = "Java unit testing";
    public static final String testEventDesc1 = "This is a description.";
    public static final Date now = new Date();
    public static Date testEventDateTime1 = null;
    public static final String testEventAddress1_1 = "Computer Science";
    public static final String testEventAddress2_1 = null;
    public static final String testEventPostcode1 = "BA2 7AY";

    public UserProfile user;
    public Event testEvent;

    public void setDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DATE, 1);

        testEventDateTime1 = c.getTime();
    }

    @Before
    public void setOrganiserUser() throws Exception {
        user = new UserProfile(UserUnitTest.testToken1, UserUnitTest.testID1);
    }

    @Before
    public void createEvent() throws IOException, JSONException {
        setDate();
        EventCreate eventCreate = new EventCreate();
        OutputPair out = eventCreate.createEvent(testEventType1, testEventTitle1, testEventDesc1,
                testEventDateTime1, testEventAddress1_1, testEventAddress2_1,
                testEventPostcode1, null, user.getAuthHeaderValue());
        if (!out.isSuccess()) {
            fail(out.getMessage());
        }
        assertEquals("Event has been created.", out.getMessage());
        testEvent = Event.getByDate(testEventDateTime1, user).get(0);
    }

    @After
    public void deleteEvent() {
        OutputPair status = testEvent.delete();

        assertTrue(status.isSuccess());
    }

    /**
     * How to get an event by its id.
     */
    @Test
    public void getAttributes() {
        try {
            System.out.println(testEvent.getTitle());
            assertEquals(testEventTitle1, testEvent.getTitle());
            assertEquals(testEventDesc1, testEvent.getDescription());
            assertEquals(true, testEvent.eventRequesterIsOrganiser());
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

    @Ignore
    public void daysUntilEvent() {
        try {
            Event event = new Event(testEventID, user);
            System.out.println(event.daysUntilEvent());
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

    /**
     * How to get an event by a search term in the title.
     */
    @Ignore
    public void searchEvent() {
        try {
            List<Event> output = Event.search("Run", user);
            assertEquals(testEventID, output.get(0).getID());
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

    @Ignore
    public void rsvpCorrect() {
        int rsvpID = createRSVP();
        deleteRSVP(rsvpID);
    }

    public void deleteRSVP(int rsvpID) {
        try {
            // get rsvp
            RSVP r = new RSVP(rsvpID, user.getAuthHeaderValue());
            // delete rsvp
            OutputPair out = r.delete();
            assertTrue(out.isSuccess());
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

    public int createRSVP() {
        try {
            // create rsvp
            OutputPair out = RSVP.create(user.getID(), testEventID, user.getAuthHeaderValue());

            assertTrue(out.isSuccess());
            int rsvpID = Integer.parseInt(out.getMessage());
            RSVP r = new RSVP(rsvpID, user.getAuthHeaderValue());
            assertEquals(testEventID, r.getEventID());
            assertEquals(UserUnitTest.testID1, r.getUserID());
            return r.getID();
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
        return -1;
    }

    @Test
    public void getEventsList() {
        try {
            List<Event> events = Event.getEventsList(user);
            System.out.println(events);
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

    @Test
    public void getEventsByType() {
        try {
            List<Event> events = Event.getByEventType("Other", user);
            System.out.println(events);
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

    @Test
    public void getEventsByTrending() {
        try {
            List<Event> events = Event.getTrendingEvents(user);
            System.out.println(events);
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }


    @Ignore
    public void getEventRSVPs() {
        try {
            Event e = new Event(testEventID, user);
            e.getRsvp();
        } catch (Exception err) {
            err.printStackTrace();
            fail(err.getMessage());
        }
    }

    @Test
    public void stringToDate() {
        String date = "2023-03-19T23:59:00Z";
        try {
            Date dateObj = Util.stringToDate(date);
        } catch (ParseException err) {
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

