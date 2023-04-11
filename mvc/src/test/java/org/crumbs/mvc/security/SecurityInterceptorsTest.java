package org.crumbs.mvc.security;

import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;

public class SecurityInterceptorsTest implements SecurityInterceptor {
    @Override
    public boolean preHandle(Request request, Response response) {
        System.out.println("Custom Security interceptor");
        return true;
    }
}
