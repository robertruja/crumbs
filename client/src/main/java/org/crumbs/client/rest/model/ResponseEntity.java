package org.crumbs.client.rest.model;

import lombok.Getter;
import lombok.Setter;
import org.crumbs.client.http.model.HttpStatus;
import org.crumbs.client.http.model.Response;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ResponseEntity<T> {

    private HttpStatus status;
    private Response response;
    private Map<String, List<String>> headers;
    private T body;
}
