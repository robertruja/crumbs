package org.crumbs.http.mvc.http;

import org.crumbs.core.annotation.Crumb;

@Crumb
public interface CrumbsMVCDispatcher {
    void handle(Request request, Response response);
}
