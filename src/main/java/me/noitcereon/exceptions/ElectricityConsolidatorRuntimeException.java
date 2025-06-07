package me.noitcereon.exceptions;

/**
 * A custom general purpose RuntimeException.
 * Meant to help differentiate between a regular RuntimeException and one made by the application.
 */
public class ElectricityConsolidatorRuntimeException extends RuntimeException {

    public ElectricityConsolidatorRuntimeException() {
        super();
    }

    public ElectricityConsolidatorRuntimeException(Throwable cause) {
        super(cause);
    }

    public ElectricityConsolidatorRuntimeException(String message) {
        super(message);
    }

    public ElectricityConsolidatorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElectricityConsolidatorRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
