package org.crumbs.mvc.interceptor;

import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

import java.util.function.BiConsumer;

@Order(1)
public class SecondInterceptor implements HandlerInterceptor {
    @Override
    public void handle(Request request, Response response, BiConsumer<Request, Response> exchange) {
        request.setAttribute("infoFromInt", 22);
        System.out.println("called second interceptor");
        exchange.accept(request, response);
    }
}
