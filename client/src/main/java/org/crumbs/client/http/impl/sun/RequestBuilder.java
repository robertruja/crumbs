package org.crumbs.client.http.impl.sun;

import org.crumbs.client.http.model.HttpMethod;
import org.crumbs.client.http.model.Request;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestBuilder {

    private HttpMethod httpMethod = HttpMethod.GET;
    private URL url;
    private Map<String, List<String>> headers = new HashMap<>();
    private byte[] payload = new byte[0];
    private InputStream payloadInput;
    private RequestBuilder() {
    }

    public static RequestBuilder newRequest() {
        return new RequestBuilder();
    }

    public RequestBuilder method(HttpMethod method) {
        this.httpMethod = method;
        return this;
    }

    public RequestBuilder url(String urlString) {
        try {
            this.url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public RequestBuilder header(String key, List<String> values) {
        headers.put(key, values);
        return this;
    }

    public RequestBuilder payload(byte[] payload) {
        this.payload = payload;
        this.payloadInput = null;
        return this;
    }

    public RequestBuilder payloadInput(InputStream inputStream) {
        this.payloadInput = inputStream;
        this.payload = new byte[0];
        return this;
    }

    public Request build() {
        if (url == null) {
            throw new RuntimeException("Url is not set for this request");
        }
        RequestImpl request = new RequestImpl();
        request.setHttpMethod(httpMethod);
        request.setUrl(url);
        request.setHeaders(headers);
        request.setPayload(payload);
        request.setPayloadInput(payloadInput);
        return request;
    }
}
