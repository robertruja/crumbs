package org.crumbs.http.client.rest.model;

import org.crumbs.http.client.http.model.HttpMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class RequestEntity<T> {

    private HttpMethod method;
    private String url;
    private T body;
    private Map<String, List<String>> headers;

}
