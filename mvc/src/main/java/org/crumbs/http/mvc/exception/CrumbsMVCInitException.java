package org.crumbs.http.mvc.exception;

public class CrumbsMVCInitException extends RuntimeException {
    public CrumbsMVCInitException(String message) {
        super(message);
    }

    public CrumbsMVCInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
