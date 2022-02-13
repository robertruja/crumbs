package org.crumbs.mvc.security.cors;

import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.common.model.HttpStatus;
import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;
import org.crumbs.mvc.interceptor.HandlerInterceptor;

public class CorsHandlerInterceptor implements HandlerInterceptor {

    private static final String CORS_ALLOW_ORIGIN = "*";
    private static final String CORS_ALLOW_METHODS = "GET,POST,PUT,DELETE,OPTIONS";
    private static final String CORS_ALLOW_HEADERS = "*";
    private static final String CORS_CACHE_MAX_AGE_SECONDS = "300";

    @Override
    public boolean handle(Request request, Response response) {
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
                return false;
            }
        }

        if (request.getHeader("Origin") != null) {
            response.addHeader("Access-Control-Allow-Origin", CORS_ALLOW_ORIGIN);
        }
        return true;
    }
}
