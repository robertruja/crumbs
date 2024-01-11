package org.crumbs.http.client.rest;

import org.crumbs.http.client.rest.model.RequestEntity;
import org.crumbs.http.client.rest.model.ResponseEntity;
import org.crumbs.core.annotation.Crumb;

@Crumb
public interface RestClient {

    <T, U> ResponseEntity<T> doRequest(RequestEntity<U> requestEntity, Class<T> clazz);
}
