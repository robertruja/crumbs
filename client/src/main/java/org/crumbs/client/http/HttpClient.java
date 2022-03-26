package org.crumbs.client.http;

import org.crumbs.client.http.model.Request;
import org.crumbs.client.http.model.Response;
import org.crumbs.core.annotation.Crumb;

@Crumb
public interface HttpClient {
    Response doRequest(Request request);
}
