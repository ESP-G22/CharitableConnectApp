package usertest;

import org.junit.Test;
import static org.junit.Assert.*;
import errors.EmptyInputError;
import errors.MaximumInputSizeError;
import user.UserValidate;

public class UserUnitTest {
    @Test
    public void EmptyEmail() {
        assertThrows(
                EmptyInputError.class,
                () -> {
                    UserValidate.checkEmail("");
                });
    }

    @Test
    public void ExpectedEmail() {
        UserValidate.checkEmail("foo@bar.com");
    }

    @Test
    public void IncorrectEmail() {
        UserValidate.checkEmail("foobar.com");
    }

    @Test
    public void MaximumEmail() {
        assertThrows(
                MaximumInputSizeError.class,
                () -> {
                    UserValidate.checkEmail(
                            "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                });
    }
}