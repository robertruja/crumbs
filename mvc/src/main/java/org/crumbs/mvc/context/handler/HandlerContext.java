package org.crumbs.mvc.context.handler;


import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbInit;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.context.CrumbsContext;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.exception.CrumbsMVCInitException;
import org.crumbs.mvc.exception.HandlerNotFoundException;
import org.crumbs.mvc.exception.HttpMethodNotAllowedException;
import org.crumbs.mvc.exception.NotFoundException;
import org.crumbs.mvc.http.Request;
import org.crumbs.mvc.http.Response;
import org.crumbs.mvc.interceptor.HandlerInterceptor;
import org.crumbs.mvc.interceptor.Order;
import org.crumbs.mvc.security.SecurityInterceptor;
import org.crumbs.mvc.security.cors.CorsHandlerInterceptor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Crumb
public class HandlerContext {

    @CrumbRef
    private CrumbsContext context;

    private Map<String, RootHandler> rootHandlers = new HashMap<>();
    private List<HandlerInterceptor> interceptors;

    @CrumbInit
    public void init() {
        List<?> handlerCrumbs = context.getCrumbsWithAnnotation(HandlerRoot.class);
        handlerCrumbs.forEach(handlerCrumb -> {
            RootHandler rootHandler = RootHandler.fromRootHandlerCrumb(handlerCrumb);
            String rootPath = rootHandler.getRootPath();
            if(rootHandlers.containsKey(rootPath)) {
                throw new CrumbsMVCInitException("Conflict: duplicate root handler paths: " + rootPath);
            }
            rootHandlers.put(rootPath, rootHandler);
        });

        interceptors = context.getCrumbsWithInterface(HandlerInterceptor.class)
                .stream()
                .sorted(Comparator.comparing(crumb -> crumb instanceof CorsHandlerInterceptor ? -1 : 1,
                        Integer::compareTo)
                        .thenComparing(crumb -> SecurityInterceptor.class.isAssignableFrom(crumb.getClass()) ? -1 : 1,
                                Integer::compareTo)
                        .thenComparing(crumb -> {
                            Order order = crumb.getClass().getAnnotation(Order.class);
                            return order == null ? 0 : order.value();
                        }, Integer::compareTo))
                .collect(Collectors.toList());
    }

    public boolean intercept(Request request, Response response) {
        boolean shouldContinue = true;
        for (HandlerInterceptor interceptor : interceptors) {
            if (!interceptor.handle(request, response)) {
                shouldContinue = false;
                break;
            }
        }
        return shouldContinue;
    }

    public Handler findHandler(Request request) {
        String reqUrlPath = request.getUrlPath();
        int subPathIdx = reqUrlPath.indexOf("/", 1);
        String requestRootPath = reqUrlPath.substring(0, subPathIdx);
        String subPath = requestRootPath.substring(subPathIdx);

        RootHandler rootHandler = rootHandlers.get(requestRootPath);
        if(rootHandler == null) {
            throw new NotFoundException(reqUrlPath);
        }
        return rootHandler.findHandler(subPath);
    }
}
