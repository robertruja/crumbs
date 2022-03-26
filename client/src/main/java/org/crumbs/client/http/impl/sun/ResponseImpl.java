package org.crumbs.client.http.impl.sun;

import org.crumbs.client.http.model.HttpStatus;
import org.crumbs.client.http.model.Response;
import org.crumbs.core.util.IOUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ResponseImpl implements Response {

    private HttpStatus status;
    private Map<String, List<String>> headers;
    private InputStream payload;


    ResponseImpl() {
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public byte[] getBody() {
        return IOUtils.readInputStream(payload);
    }

    @Override
    public InputStream getInputStream() {
        return payload;
    }

    void setStatusCode(HttpStatus status) {
        this.status = status;
    }

    void setPayload(InputStream responsePayload) {
        this.payload = responsePayload;
    }
}
