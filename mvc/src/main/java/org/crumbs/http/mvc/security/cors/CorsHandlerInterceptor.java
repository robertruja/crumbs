package org.crumbs.http.mvc.security.cors;

import org.crumbs.http.mvc.interceptor.HandlerInterceptor;
import org.crumbs.http.common.model.HttpMethod;
import org.crumbs.http.common.model.HttpStatus;
import org.crumbs.http.mvc.http.Request;
import org.crumbs.http.mvc.http.Response;

public class CorsHandlerInterceptor implements HandlerInterceptor {

    private static final String CORS_ALLOW_ORIGIN = "*";
    private static final String CORS_ALLOW_METHODS = "GET,POST,PUT,DELETE,OPTIONS";
    private static final String CORS_ALLOW_HEADERS = "*";
    private static final String CORS_CACHE_MAX_AGE_SECONDS = "300";

    @Override
    public boolean preHandle(Request request, Response response) {
        if (request.getMethod().equals(HttpMethod.OPTIONS)) {
            // Javascript's fetch preflight request
            if (request.getHeader("Access-Control-Request-Method") != null &&
                    request.getHeader("Access-Control-Request-Headers") != null &&
                    request.getHeader("Origin") != null) {
                response.addHeader("Access-Control-Allow-Origin", CORS_ALLOW_ORIGIN);
                response.addHeader("Access-Control-Allow-Methods", CORS_ALLOW_METHODS);
                response.addHeader("Access-Control-Allow-Headers", CORS_ALLOW_HEADERS);
                response.addHeader("Access-Control-Max-Age", CORS_CACHE_MAX_AGE_SECONDS);
                response.setStatus(HttpStatus.OK);
            }
            return false;
        }

        if (request.getHeader("Origin") != null) {
            response.addHeader("Access-Control-Allow-Origin", CORS_ALLOW_ORIGIN);
        }
        return true;
    }
}
