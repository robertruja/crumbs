package org.crumbs.core.logging.exeption;

public class LoggerInitializationException extends RuntimeException {
    public LoggerInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
