package layout;

class Pair<T, U> {
    public T x;
    public U y;

    public Pair(T x, U y) {
        this.x = x;
        this.y = y;
    }
}

public class OutputPair extends Pair<Boolean, String> {
    public OutputPair(Boolean success, String message) {
        super(success, message);
    }

    public boolean isSuccess() {
        return x;
    }

    public String getMessage() {
        return y;
    }
}
