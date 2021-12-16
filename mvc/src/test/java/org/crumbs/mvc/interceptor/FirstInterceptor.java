package org.crumbs.mvc.interceptor;

import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

@Order(2)
public class FirstInterceptor implements HandlerInterceptor {
    @Override
    public boolean handle(Request request, Response response) {
        request.setAttribute("infoFromInterceptor1", "some info interceptor 1");
        System.out.println("called first interceptor");
        return true;
    }
}
