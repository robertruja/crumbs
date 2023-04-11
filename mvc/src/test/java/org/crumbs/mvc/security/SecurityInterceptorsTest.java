package org.crumbs.mvc.security;

import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

import java.util.function.BiConsumer;

public class SecurityInterceptorsTest implements SecurityInterceptor {
    @Override
    public void handle(Request request, Response response, BiConsumer<Request, Response> exchange) {
        System.out.println("Custom Security interceptor");
        exchange.accept(request, response);
    }
}
