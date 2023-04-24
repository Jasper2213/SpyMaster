package util;

public class SpyMasterException extends RuntimeException {

    public SpyMasterException() {}

    public SpyMasterException(String message) {
        super(message);
    }

    public SpyMasterException(String message, Throwable t) {
        super(message, t);
    }

}
