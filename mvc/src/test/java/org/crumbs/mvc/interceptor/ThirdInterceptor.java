package org.crumbs.mvc.interceptor;

import org.crumbs.mvc.common.model.HttpStatus;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

import java.util.function.BiConsumer;

@Order(3)
public class ThirdInterceptor implements HandlerInterceptor {
    @Override
    public void handle(Request request, Response response, BiConsumer<Request, Response> exchange) {
        response.setStatus(HttpStatus.FORBIDDEN);
        response.setBody("Not allowed".getBytes());
        response.setMime(Mime.TEXT_PLAIN);
        if(!request.getUrlPath().contains("invalid")) {
            exchange.accept(request, response);
        }
    }
}
