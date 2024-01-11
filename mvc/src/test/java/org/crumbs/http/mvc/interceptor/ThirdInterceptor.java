package org.crumbs.http.mvc.interceptor;

import org.crumbs.http.common.model.HttpStatus;
import org.crumbs.http.common.model.Mime;
import org.crumbs.http.mvc.http.Request;
import org.crumbs.http.mvc.http.Response;

@Order(3)
public class ThirdInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(Request request, Response response) {
        response.setStatus(HttpStatus.FORBIDDEN);
        response.setBody("Not allowed".getBytes());
        response.setMime(Mime.TEXT_PLAIN);
        if (!request.getUrlPath().contains("invalid")) {
            return true;
        }
        return false;
    }
}
