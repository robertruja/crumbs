package org.crumbs.http.client.http.model;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface Request {

    HttpMethod getMethod();

    String getUrlPath();

    URL getUrl();

    byte[] getPayload();

    Map<String, List<String>> getHeaders();

    InputStream getPayloadInput();
}
