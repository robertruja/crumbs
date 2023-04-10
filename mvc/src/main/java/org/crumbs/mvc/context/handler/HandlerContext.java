package org.crumbs.mvc.context.handler;


import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbInit;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.context.CrumbsContext;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.common.model.Mime;
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

import java.util.*;
import java.util.stream.Collectors;

@Crumb
public class HandlerContext {

    @CrumbRef
    private CrumbsContext context;

    private HandlerTreeNode root = new HandlerTreeNode();
    private List<HandlerInterceptor> interceptors;

    @CrumbInit
    public void init() {
        List<?> handlerCrumbs = context.getCrumbsWithAnnotation(HandlerRoot.class);
        handlerCrumbs.forEach(this::addToTree);

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

    public Map<HttpMethod,Handler> findHandler(Request request) {
        String reqUrlPath = request.getUrlPath();
        List<String> source = subPaths(reqUrlPath);
        return findRecursively(source.iterator(), root);
    }

    private void addToTree(Object handlerCrumb) {

        Class<?> clazz = handlerCrumb.getClass();

        String rootPath = clazz.getAnnotation(HandlerRoot.class).value();

        if (!rootPath.startsWith("/")) {
            throw new CrumbsMVCInitException("Invalid root handler path: " + rootPath + ". Must start with '/'");
        }
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(org.crumbs.mvc.annotation.Handler.class) != null)
                .forEach(method -> {
                    org.crumbs.mvc.annotation.Handler annotation =
                            method.getAnnotation(org.crumbs.mvc.annotation.Handler.class);

                    String subPath = annotation.mapping();
                    if (!subPath.startsWith("/")) {
                        throw new CrumbsMVCInitException("Invalid handler path: " + subPath + ". Must start with '/'");
                    }
                    HttpMethod httpMethod = annotation.method();
                    Mime mime = annotation.producesContent();
                    Handler handler = new Handler();
                    handler.handlerRootInstance = handlerCrumb;
                    handler.httpMethod = httpMethod;
                    handler.responseContentType = mime;
                    handler.method = method;
                    handler.path = rootPath.equals("/") ? subPath : rootPath + subPath;
                    handler.paramList = Handler.buildParamList(method);
                    addHandler(handler.path, httpMethod, handler);
                });
    }

    private void addHandler(String path, HttpMethod method, Handler handler) {
        List<String> split = subPaths(path);
        Map<HttpMethod, Handler> existingByPath = findRecursively(split.iterator(), root);
        if(existingByPath != null) {
            Handler existing = existingByPath.get(method);
            if(existing != null) {
                throw new RuntimeException("Conflict: Path " + path + " is already defined with: " + existing.getPath());
            }
        }
        addRecursively(split.iterator(), root, method, handler);
    }

    private Map<HttpMethod, Handler> findRecursively(Iterator<String> iterator, HandlerTreeNode treeNode) {
        if(treeNode == null) {
            return null;
        }
        if(iterator.hasNext()) {
            return findRecursively(iterator, treeNode.getChild(iterator.next()));
        }
        return treeNode.getHandlers();
    }

    private void addRecursively(Iterator<String> it, HandlerTreeNode treeNode, HttpMethod method, Handler handler) {
        if(it.hasNext()) {
            String nextChildPath = it.next();
            HandlerTreeNode existingChild = treeNode.getChild(nextChildPath);
            if(existingChild == null) {
                existingChild = new HandlerTreeNode();
                treeNode.addSubmapping(nextChildPath, existingChild);
            }
            addRecursively(it, existingChild, method, handler);
        } else {
            treeNode.putHandler(method, handler);
        }
    }

    private List<String> subPaths(String path) {
        List<String> result = new LinkedList<>();
        if(path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if(!path.startsWith("/")) {
            throw new RuntimeException("The path " + path + " does not start with '/'");
        }
        if(path.equals("/")) {
            return result;
        }
        Arrays.stream(path.substring(1).split("/"))
                .forEach(subPath -> result.add("/" + subPath));
        return result;
    }
}
