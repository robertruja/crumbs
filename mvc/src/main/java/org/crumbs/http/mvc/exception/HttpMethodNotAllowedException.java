package org.crumbs.http.mvc.exception;

public class HttpMethodNotAllowedException extends RuntimeException {
    public HttpMethodNotAllowedException(String message) {
        super(message);
    }
}
