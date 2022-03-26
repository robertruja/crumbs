package org.crumbs.mvc.model;

import org.crumbs.mvc.common.model.HttpStatus;

public class ResponseEntity<T> {
    private T body;
    private HttpStatus status;
    private ResponseEntity(HttpStatus status, T body) {
        this.body = body;
        this.status = status;
    }

    public static ResponseEntityBuilder status(HttpStatus status) {
        return new ResponseEntityBuilder(status);
    }

    public T getBody() {
        return body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static class ResponseEntityBuilder {
        private HttpStatus status;

        public ResponseEntityBuilder(HttpStatus status) {
            this.status = status;
        }

        public <U> ResponseEntity<U> body(U body) {
            return new ResponseEntity<>(status, body);
        }

        public ResponseEntity<?> emptyBody() {
            return new ResponseEntity<>(status, null);
        }
    }
}
