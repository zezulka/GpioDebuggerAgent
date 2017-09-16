package request;

public class IllegalRequestException extends Exception {

    public IllegalRequestException() {
        super();
    }

    public IllegalRequestException(String msg) {
        super(msg);
    }

    public IllegalRequestException(Throwable cause) {
        super(cause);
    }

    public IllegalRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
