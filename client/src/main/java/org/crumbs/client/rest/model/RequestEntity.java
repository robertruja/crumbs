package org.crumbs.client.rest.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.crumbs.client.http.model.HttpMethod;

@Getter
@Setter
@Builder
public class RequestEntity<T> {

    private HttpMethod method;
    private String url;
    private T body;

}
