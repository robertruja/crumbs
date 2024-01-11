package org.crumbs.http.mvc.interceptor;

import org.crumbs.http.mvc.http.Request;
import org.crumbs.http.mvc.http.Response;
import org.crumbs.http.mvc.interceptor.HandlerInterceptor;
import org.crumbs.http.mvc.interceptor.Order;

@Order(1)
public class SecondInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(Request request, Response response) {
        request.setAttribute("infoFromInt", 22);
        System.out.println("called second interceptor");
        return true;
    }
}
