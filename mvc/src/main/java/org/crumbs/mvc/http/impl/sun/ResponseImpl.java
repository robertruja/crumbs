package org.crumbs.mvc.http.impl.sun;

import com.sun.net.httpserver.HttpExchange;
import org.crumbs.mvc.common.model.HttpStatus;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.http.Response;

public class ResponseImpl implements Response {

    private HttpExchange exchange;
    private HttpStatus status;
    private byte[] body = new byte[0];

    public ResponseImpl(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void setMime(Mime mime) {
        exchange.getResponseHeaders().set("Content-Type", mime.getValue());
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void addHeader(String key, String value) {
        exchange.getResponseHeaders().set(key, value);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public byte[] getBody() {
        return body;
    }
}
