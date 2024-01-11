package org.crumbs.http.mvc.interceptor;

import org.crumbs.core.annotation.Crumb;
import org.crumbs.http.mvc.http.Request;
import org.crumbs.http.mvc.http.Response;

@Crumb
public interface HandlerInterceptor {

    default boolean preHandle(Request request, Response response) {
        return true;
    }

    default void postHandle(Request request, Response response) {

    }
}
