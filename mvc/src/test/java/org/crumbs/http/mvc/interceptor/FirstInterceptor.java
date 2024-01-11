package org.crumbs.http.mvc.interceptor;

import org.crumbs.http.mvc.http.Request;
import org.crumbs.http.mvc.http.Response;
import org.crumbs.http.mvc.interceptor.HandlerInterceptor;
import org.crumbs.http.mvc.interceptor.Order;

@Order(2)
public class FirstInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(Request request, Response response) {
        request.setAttribute("infoFromInterceptor1", "some info interceptor 1");
        System.out.println("called first interceptor");
        return true;
    }
}
