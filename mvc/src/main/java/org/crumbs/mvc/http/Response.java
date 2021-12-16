package org.crumbs.mvc.http;

import org.crumbs.mvc.common.model.HttpStatus;
import org.crumbs.mvc.common.model.Mime;

public interface Response {

    void setStatus(HttpStatus status);
    void setBody(byte[] body);
    void setMime(Mime mime);
    void addHeader(String key, String val);
    HttpStatus getStatus();
    byte[] getBody();
}
