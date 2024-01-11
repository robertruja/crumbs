package org.crumbs.http.mvc.exception;

public class HandlerInvocationException extends RuntimeException {
    public HandlerInvocationException(String message) {
        super(message);
    }

    public HandlerInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
