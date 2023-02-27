package event;

import org.junit.Test;
import static org.junit.Assert.*;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;

public class EventUnitTest {
    @Test
    public void EmptyTitle() {
        assertThrows(
                EmptyInputError.class,
                () -> {
                    EventValidate.checkTitle("");
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
                    EventValidate.checkTitle(
                            "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                });
    }

    @Test
    public void EmptyDescription() {
        assertThrows(
                EmptyInputError.class,
                () -> {
                    EventValidate.checkDescription("");
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
                    EventValidate.checkDescription(
                            "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                });
    }

    @Test
    public void nullDatetime() {
        assertTrue(False);
    }

    @Test
    public void FutureDatetime() {
        assertTrue(False);
    }

    @Test
    public void locationNotInBA2() {
        assertTrue(False);
    }

    @Test
    public void locationInBA2() {
        assertTrue(False);
    }
}
