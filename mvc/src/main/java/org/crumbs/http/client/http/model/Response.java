package org.crumbs.http.client.http.model;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface Response {
    Map<String, List<String>> getHeaders();

    HttpStatus getStatus();

    byte[] getBody();

    InputStream getInputStream();
}
