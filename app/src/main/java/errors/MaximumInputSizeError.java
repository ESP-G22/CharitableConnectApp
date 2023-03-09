package errors;

public class MaximumInputSizeError extends Exception {
    public MaximumInputSizeError(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
