package eventtest;

import org.junit.Test;
import static org.junit.Assert.*;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;
import validate.Event;

public class EventUnitTest {
    @Test
    public void EmptyTitle() {
        assertThrows(
                EmptyInputError.class,
                () -> {
                    Event.checkTitle("");
                });
    }

    @Test
    public void ExpectedTitle() {
        Event.checkTitle("ah");
    }

    @Test
    public void MaximumTitle() {
        assertThrows(
                MaximumInputSizeError.class,
                () -> {
                    Event.checkTitle(
                            "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                });
    }

    @Test
    public void EmptyDescription() {
        assertThrows(
                EmptyInputError.class,
                () -> {
                    Event.checkDescription("");
                });
    }

    @Test
    public void ExpectedDescription() {
        Event.checkDescription("ah");
    }

    @Test
    public void MaximumDescription() {
        assertThrows(
                MaximumInputSizeError.class,
                () -> {
                    Event.checkDescription(
                            "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                });
    }

    @Test
    public void nullDatetime() {
        assertTrue(false);
    }

    @Test
    public void FutureDatetime() {
        assertTrue(false);
    }

    @Test
    public void locationNotInBA2() {
        assertTrue(false);
    }

    @Test
    public void locationInBA2() {
        assertTrue(false);
    }
}
