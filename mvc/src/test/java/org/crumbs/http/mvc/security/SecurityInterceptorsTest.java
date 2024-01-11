package org.crumbs.http.mvc.security;

import org.crumbs.http.mvc.http.Request;
import org.crumbs.http.mvc.http.Response;
import org.crumbs.http.mvc.security.SecurityInterceptor;

public class SecurityInterceptorsTest implements SecurityInterceptor {
    @Override
    public boolean preHandle(Request request, Response response) {
        System.out.println("Custom Security interceptor");
        return true;
    }
}
