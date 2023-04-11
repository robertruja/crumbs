package org.crumbs.mvc.interceptor;

import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

@Order(1)
public class SecondInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(Request request, Response response) {
        request.setAttribute("infoFromInt", 22);
        System.out.println("called second interceptor");
        return true;
    }
}
