package org.crumbs.mvc.interceptor;

import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

import java.util.function.BiConsumer;

@Order(2)
public class FirstInterceptor implements HandlerInterceptor {
    @Override
    public void handle(Request request, Response response, BiConsumer<Request, Response> exchange) {
        request.setAttribute("infoFromInterceptor1", "some info interceptor 1");
        System.out.println("called first interceptor");
        exchange.accept(request, response);
    }
}
