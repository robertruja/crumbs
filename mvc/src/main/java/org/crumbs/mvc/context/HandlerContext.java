package org.crumbs.mvc.context;


import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbInit;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.context.CrumbsContext;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.exception.HandlerNotFoundException;
import org.crumbs.mvc.exception.HttpMethodNotAllowedException;
import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;
import org.crumbs.mvc.interceptor.HandlerInterceptor;
import org.crumbs.mvc.interceptor.Order;

import java.util.*;
import java.util.stream.Collectors;

@Crumb
public class HandlerContext {

    @CrumbRef
    private CrumbsContext context;

    private Map<String, Map<HttpMethod, Handler>> handlerMap = new HashMap<>();
    private List<HandlerInterceptor> interceptors;

    @CrumbInit
    public void init() {
        List<?> handlerCrumbs = context.getCrumbsWithAnnotation(HandlerRoot.class);
        handlerCrumbs.forEach(handlerCrumb -> {
            List<Handler> handlers = Handler.fromObject(handlerCrumb);
            handlers.forEach(handler -> {
                Map<HttpMethod, Handler> httpMethodMapping = handlerMap.get(handler.getURI());
                if(httpMethodMapping == null) {
                    httpMethodMapping = new HashMap<>();
                }
                httpMethodMapping.put(handler.getHttpMethod(), handler);
                handlerMap.put(handler.getURI(), httpMethodMapping);
            });
        });
        interceptors = context.getCrumbsWithInterface(HandlerInterceptor.class)
                .stream()
                .sorted((int1, int2) -> {
                    Order order1 = int1.getClass().getAnnotation(Order.class);
                    Order order2 = int2.getClass().getAnnotation(Order.class);
                    int intOrder1 = order1 == null ? 0 : order1.value();
                    int intOrder2 = order2 == null ? 0 : order2.value();
                    return Integer.compare(intOrder2, intOrder1);
                })
                .collect(Collectors.toList());
    }

    public boolean intercept(Request request, Response response) {
        boolean shouldContinue = true;
        for(HandlerInterceptor interceptor: interceptors) {
            if(!interceptor.handle(request, response)) {
                shouldContinue = false;
                break;
            }
        }
        return shouldContinue;
    }

    public HandlerInvocationResult invokeHandler(Request request) throws Exception {
        Map<HttpMethod, Handler> httpMethodMapping = findPath(request);
        if(httpMethodMapping == null) {
            throw new HandlerNotFoundException("Could not find handler for resource: " + request.getUrlPath());
        }
        Handler handler = httpMethodMapping.get(request.getMethod());
        if(handler == null) {
            throw new HttpMethodNotAllowedException("There is no mapping for " + request.getMethod()
                    + " for path " + request.getUrlPath());
        }
        return handler.invoke(request);
    }

    private Map<HttpMethod, Handler> findPath(Request request) {
        mapping: for(String mapping: handlerMap.keySet()) {
            String[] subMappings = mapping.substring(1).split("/");
            String[] subPaths = request.getUrlPath().substring(1).split("/");
            if(subMappings.length == subPaths.length) {
                for (int i = 0; i < subMappings.length; i++) {
                    String subMapping = subMappings[i];
                    String subPath = subPaths[i];
                    if(!subMapping.equals(subPath)) {
                        if(subMapping.startsWith("{") && subMapping.endsWith("}")) {
                            request.setPathVariable(subMapping.substring(1, subMapping.length() - 1), subPath);
                        } else {
                            continue mapping;
                        }
                    }
                }
            } else {
                continue;
            }
            return handlerMap.get(mapping);
        }
        return null;
    }
}
