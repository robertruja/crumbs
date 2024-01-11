package org.crumbs.http.mvc.http;

import org.crumbs.http.common.model.HttpStatus;
import org.crumbs.http.common.model.Mime;

import java.io.OutputStream;

public interface Response {

    void setMime(Mime mime);

    void addHeader(String key, String val);

    HttpStatus getStatus();

    void setStatus(HttpStatus status);

    byte[] getBody();

    void setBody(byte[] body);

    OutputStream getOutputStream();

    void flush();
}
