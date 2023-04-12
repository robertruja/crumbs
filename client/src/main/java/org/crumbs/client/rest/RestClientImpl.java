package org.crumbs.client.rest;

import org.crumbs.client.exception.RestClientException;
import org.crumbs.client.http.HttpClient;
import org.crumbs.client.http.impl.sun.RequestBuilder;
import org.crumbs.client.http.model.Mime;
import org.crumbs.client.http.model.Response;
import org.crumbs.client.rest.model.RequestEntity;
import org.crumbs.client.rest.model.ResponseEntity;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.json.JsonMapper;
import org.crumbs.json.exception.JsonMarshalException;
import org.crumbs.json.exception.JsonUnmarshalException;

import java.util.List;
import java.util.Map;

public class RestClientImpl implements RestClient {

    private JsonMapper jsonMapper = new JsonMapper();

    @CrumbRef
    private HttpClient client;

    @Override
    public <T, U> ResponseEntity<T> doRequest(RequestEntity<U> requestEntity, Class<T> clazz) {

        RequestBuilder builder = RequestBuilder.newRequest()
                .method(requestEntity.getMethod())
                .url(requestEntity.getUrl());

        U requestBody = requestEntity.getBody();

        if (requestBody != null) {
            try {
                if(requestBody instanceof String) {
                    builder.payload(((String) requestBody).getBytes());
                } else if (requestBody instanceof byte[]) {
                    builder.payload((byte[])requestBody);
                } else {
                    builder.payload(jsonMapper.marshal(requestBody).getBytes());
                }
            } catch (JsonMarshalException | IllegalAccessException e) {
                throw new RestClientException("Unable to serialize request body ", e);
            }
        }

        Map<String, List<String>> headers = requestEntity.getHeaders();
        if (headers == null) {
            builder.header("Accept", List.of("application/json"));
        } else {
            headers.forEach(builder::header);
        }

        Response response = client.doRequest(builder.build());

        ResponseEntity<T> restResponse = new ResponseEntity<>();
        restResponse.setStatus(response.getStatus());
        restResponse.setResponse(response);
        restResponse.setHeaders(response.getHeaders());

        if (hasMime(Mime.APPLICATION_JSON, response.getHeaders())) {
            try {
                T responseBody = jsonMapper.unmarshal(response.getBody(), clazz);
                restResponse.setBody(responseBody);

            } catch (JsonUnmarshalException e) {
                throw new RestClientException("Unable to deserialize response body", e);
            }
        } else if (String.class.equals(clazz)) {
            restResponse.setBody((T) new String(response.getBody()));
        } else {
            throw new RestClientException("Cannot deserialize response to class: " + clazz.getName());
        }
        return restResponse;
    }

    private boolean hasMime(Mime mime, Map<String, List<String>> headers) {
        List<String> contentType = headers.get("Content-Type");
        return contentType != null && contentType.stream().anyMatch(ctype -> mime.equals(Mime.fromValue(ctype)));
    }
}
