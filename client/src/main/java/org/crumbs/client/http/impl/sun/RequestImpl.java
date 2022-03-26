package org.crumbs.client.http.impl.sun;

import org.crumbs.client.http.model.HttpMethod;
import org.crumbs.client.http.model.Request;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class RequestImpl implements Request {

    private HttpMethod httpMethod;
    private URL url;
    private Map<String, List<String>> headers;
    private byte[] payload = new byte[0];
    private InputStream payloadInput;

    RequestImpl() {
    }


    @Override
    public HttpMethod getMethod() {
        return httpMethod;
    }

    @Override
    public String getUrlPath() {
        return url == null ? "null" : url.toString();
    }

    @Override
    public URL getUrl() {
        return url;
    }

    void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }

    void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public InputStream getPayloadInput() {
        return payloadInput;
    }

    void setPayloadInput(InputStream payloadInput) {
        this.payloadInput = payloadInput;
    }

    void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
