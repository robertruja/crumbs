package org.crumbs.http.mvc.http;

import org.crumbs.http.common.model.HttpMethod;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public interface Request {

    void setPathVariable(String name, String value);

    void setAttribute(String attribute, Object value);

    HttpMethod getMethod();

    String getUrlPath();

    Map<String, String> getQueryParams();

    Object getAttribute(String key);

    byte[] getBody();

    InputStream getBodyInputStream();

    String getBodyAsString(Charset charset);

    String getPathVarialbe(String name);

    List<String> getHeader(String key);

    Map<String, List<String>> getHeaders();

    String getSource();

}
