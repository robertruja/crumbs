package org.crumbs.client.rest.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.crumbs.client.http.model.HttpMethod;

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
