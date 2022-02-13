package org.crumbs.client.rest;

import org.crumbs.client.rest.model.RequestEntity;
import org.crumbs.client.rest.model.ResponseEntity;
import org.crumbs.core.annotation.Crumb;

@Crumb
public interface RestClient {

    <T, U> ResponseEntity<T> doRequest(RequestEntity<U> requestEntity, Class<T> clazz);
}
