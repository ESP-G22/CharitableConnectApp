package layout;

public class OutputPair extends Pair<Boolean, String> {
    public OutputPair(Boolean success, String message) {
        super(success, message);
    }

    public boolean isSuccess() {
        return arg1;
    }

    public String getMessage() {
        return arg2;
    }

    public void setMessage(String newMessage) {
        this.arg2 = newMessage;
    }
}
