package org.crumbs.mvc.http;

import org.crumbs.mvc.common.model.HttpMethod;

import java.nio.charset.Charset;
import java.util.Map;

public interface Request {

    void setPathVariable(String name, String value);
    void setAttribute(String attribute, Object value);
    HttpMethod getMethod();
    String getUrlPath();
    Map<String, String> getQueryParams();
    Object getAttribute(String key);
    byte[] getBody();
    String getBodyAsString(Charset charset);
    String getPathVarialbe(String name);

}
