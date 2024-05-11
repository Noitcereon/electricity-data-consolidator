package me.noitcereon.exceptions;

public class MissingApiKeyException extends RuntimeException{
    public MissingApiKeyException() {
        super("Missing Api key for the application to work. Have you updated the appropriate configuration in the /config folder?");
    }

    public MissingApiKeyException(String message) {
        super(message);
    }

    public MissingApiKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingApiKeyException(Throwable cause) {
        super(cause);
    }

    public MissingApiKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
