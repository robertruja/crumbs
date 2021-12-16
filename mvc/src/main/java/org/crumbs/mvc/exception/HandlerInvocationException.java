package org.crumbs.mvc.exception;

public class HandlerInvocationException extends RuntimeException {
    public HandlerInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
