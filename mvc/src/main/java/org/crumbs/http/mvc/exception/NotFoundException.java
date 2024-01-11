package org.crumbs.http.mvc.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Object resource) {
        super("Unable to find resource: " + resource);
    }
}
