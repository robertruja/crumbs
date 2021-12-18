package org.crumbs.mvc.context;

import org.crumbs.mvc.annotation.RequestParam;
import org.crumbs.mvc.annotation.*;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.exception.CrumbsMVCInitException;
import org.crumbs.mvc.exception.HandlerInvocationException;
import org.crumbs.mvc.http.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Handler {

    private String uri;
    private Object handlerRootInstance;
    private HttpMethod httpMethod;
    private Mime responseContentType;

    private Method method;
    private List<HandlerParam> paramList;

    private Handler() {}

    public static List<Handler> fromObject(Object handlerCrumb) {
        List<Handler> handlers = new ArrayList<>();
        Class<?> clazz = handlerCrumb.getClass();

        String rootPath = clazz.getAnnotation(HandlerRoot.class).value();

        if(!rootPath.startsWith("/")) {
            throw new CrumbsMVCInitException("Invalid handler path: " + rootPath + ". Must start with '/'");
        }

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(org.crumbs.mvc.annotation.Handler.class) != null)
                .forEach(method -> {
                    org.crumbs.mvc.annotation.Handler annotation =
                            method.getAnnotation(org.crumbs.mvc.annotation.Handler.class);

                    String subPath = annotation.mapping();
                    HttpMethod httpMethod = annotation.method();
                    Mime mime = annotation.producesContent();
                    Handler handler = new Handler();
                    handler.handlerRootInstance = handlerCrumb;
                    handler.httpMethod = httpMethod;
                    handler.responseContentType = mime;
                    handler.method = method;
                    handler.uri = rootPath + subPath;
                    handler.paramList = buildParamList(method);
                    handlers.add(handler);
                });
        return handlers;
    }

    public HandlerInvocationResult invoke(Request request) throws Exception {
        try {
            Object[] parameters = getParameters(request);
            Object invocationResult = method.invoke(handlerRootInstance, parameters);

            return new HandlerInvocationResult(responseContentType, invocationResult,
                    method.getReturnType());
        } catch (IllegalAccessException e) {
            throw new HandlerInvocationException("Could not invoke handler method " + method, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if(cause instanceof Error) {
                throw new HandlerInvocationException("Handler invocation threw error", e);
            } else {
                Exception exception = (Exception) e.getCause();
                throw exception;
            }
        }
    }

    private static List<HandlerParam> buildParamList(Method method) {
        return Arrays.stream(method.getParameters())
                .map(parameter -> {
                    Annotation annotation = parameter.getAnnotation(RequestBody.class);
                    if(annotation != null) {
                        return new RequestBodyParam(parameter.getType());
                    }
                    annotation = parameter.getAnnotation(RequestParam.class);
                    if(annotation != null) {
                        RequestParam requestParamAnnotation = (RequestParam) annotation;
                        return new org.crumbs.mvc.context.RequestParam(
                                requestParamAnnotation.value(), parameter.getName(), requestParamAnnotation.required(),
                                parameter.getType());
                    }
                    annotation = parameter.getAnnotation(PathVariable.class);
                    if(annotation != null) {
                        PathVariable pathVariable = (PathVariable)annotation;
                        return new PathVariableParam(parameter.getName(), pathVariable.value(), parameter.getType());
                    }
                    annotation = parameter.getAnnotation(Header.class);
                    if(annotation != null) {
                        return new HeaderParam();
                    }
                    annotation = parameter.getAnnotation(RequestAttribute.class);
                    if(annotation != null) {
                        RequestAttribute requestAttribute = (RequestAttribute) annotation;
                        return new RequestAttributeParam(parameter.getName(), requestAttribute.value(),
                                parameter.getType());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Object[] getParameters(Request request) {
        return paramList.stream()
                .map(param -> param.value(request)).toArray();
    }

    public String getURI() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}
