package org.crumbs.mvc.interceptor;

import org.crumbs.core.annotation.Crumb;
import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

import java.util.function.BiConsumer;

@Crumb
public interface HandlerInterceptor {

    void handle(Request request, Response response, BiConsumer<Request, Response> exchangeConsumer);
}
