package org.crumbs.mvc.http.impl.sun;

import com.sun.net.httpserver.HttpExchange;
import org.crumbs.mvc.common.model.HttpStatus;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.exception.InternalServerErrorException;
import org.crumbs.mvc.http.Response;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseImpl implements Response {

    private HttpExchange exchange;
    private HttpStatus status;
    private boolean handeled;
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

    @Override
    public OutputStream getOutputStream() {
        handeled = true;
        try {
            sendResponseHeaders();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exchange.getResponseBody();
    }

    @Override
    public void flush() {
        if(!handeled) {
            handeled = true;
            try {
                sendResponseHeaders();
                OutputStream os = exchange.getResponseBody();
                os.write(body);
                os.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void sendResponseHeaders() throws IOException {
        if(status == null ) {
            throw new InternalServerErrorException("Status not set on response");
        }
        exchange.sendResponseHeaders(status.getCode(), status.equals(HttpStatus.NO_CONTENT) ? - 1 : body.length);
    }
}
