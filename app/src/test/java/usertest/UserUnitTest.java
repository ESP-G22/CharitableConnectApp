package usertest;

import org.junit.Test;
import static org.junit.Assert.*;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;
import validate.User;

public class UserUnitTest {
    @Test
    public void EmptyEmail() {
        assertThrows(
                EmptyInputError.class,
                () -> {
                    User.checkEmail("");
                });
    }

    @Test
    public void ExpectedEmail() {
        User.checkEmail("foo@bar.com");
    }

    @Test
    public void IncorrectEmail() {
        User.checkEmail("foobar.com");
    }

    @Test
    public void MaximumEmail() {
        assertThrows(
                MaximumInputSizeError.class,
                () -> {
                    User.checkEmail(
                            "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                });
    }
}