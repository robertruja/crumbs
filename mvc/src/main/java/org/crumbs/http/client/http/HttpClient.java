package org.crumbs.http.client.http;

import org.crumbs.http.client.http.model.Response;
import org.crumbs.http.client.http.model.Request;
import org.crumbs.core.annotation.Crumb;

@Crumb
public interface HttpClient {
    Response doRequest(Request request);
}
