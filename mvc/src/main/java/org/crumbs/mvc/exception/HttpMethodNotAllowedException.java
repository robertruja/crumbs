package org.crumbs.mvc.exception;

public class HttpMethodNotAllowedException extends RuntimeException {
    public HttpMethodNotAllowedException(String message) {
        super(message);
    }
}
