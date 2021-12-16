package org.crumbs.mvc.interceptor;

import org.crumbs.core.annotation.Crumb;
import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

@Crumb
public interface HandlerInterceptor {

    boolean handle(Request request, Response response);
}
