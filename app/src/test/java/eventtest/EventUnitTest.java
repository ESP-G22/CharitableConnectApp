package eventtest;

import org.junit.Test;
import static org.junit.Assert.*;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;
import event.EventValidate;

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
        EventValidate.checkTitle("ah");
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
        EventValidate.checkDescription("ah");
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
