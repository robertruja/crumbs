package org.crumbs.client.exception;

public class ConnectException extends RuntimeException {
    public ConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
