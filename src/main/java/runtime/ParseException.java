package runtime;

public class ParseException extends Exception {
    public ParseException(String description) {
        super(description);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String description, Throwable cause) {
        super(description, cause);
    }
}
