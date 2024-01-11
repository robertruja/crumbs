package org.crumbs.http.client.rest.model;

import org.crumbs.http.client.http.model.HttpStatus;
import org.crumbs.http.client.http.model.Response;
import lombok.Getter;
import lombok.Setter;

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
