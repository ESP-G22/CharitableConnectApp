package errors;

public class EmptyInputError extends Exception {
    public EmptyInputError(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
