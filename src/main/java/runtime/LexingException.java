package runtime;

public class LexingException extends Exception {
    public LexingException(String description) {
        super(description);
    }

    public LexingException(Throwable cause) {
        super(cause);
    }

    public LexingException (String description, Throwable cause) {
        super(description, cause);
    }
}
