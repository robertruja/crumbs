package org.crumbs.http.mvc.context.handler;

import lombok.Getter;
import org.crumbs.http.mvc.annotation.*;
import org.crumbs.http.mvc.exception.HandlerInvocationException;
import org.crumbs.http.common.model.HttpMethod;
import org.crumbs.http.common.model.Mime;
import org.crumbs.http.mvc.context.HeaderParam;
import org.crumbs.http.mvc.context.PathVariableParam;
import org.crumbs.http.mvc.context.RequestAttributeParam;
import org.crumbs.http.mvc.context.RequestBodyParam;
import org.crumbs.http.mvc.http.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class Handler {

    String path;
    Object handlerRootInstance;
    HttpMethod httpMethod;
    Mime responseContentType;

    Method method;
    List<HandlerParam> paramList;

    Handler() {
    }

    static List<HandlerParam> buildParamList(Method method) {
        return Arrays.stream(method.getParameters())
                .map(parameter -> {
                    Annotation annotation = parameter.getAnnotation(RequestBody.class);
                    if (annotation != null) {
                        return new RequestBodyParam(parameter.getType());
                    }
                    annotation = parameter.getAnnotation(RequestParam.class);
                    if (annotation != null) {
                        RequestParam requestParamAnnotation = (RequestParam) annotation;
                        return new org.crumbs.http.mvc.context.RequestParam(
                                requestParamAnnotation.value(), parameter.getName(), requestParamAnnotation.required(),
                                parameter.getType());
                    }
                    annotation = parameter.getAnnotation(PathVariable.class);
                    if (annotation != null) {
                        PathVariable pathVariable = (PathVariable) annotation;
                        return new PathVariableParam(parameter.getName(), pathVariable.value(), parameter.getType());
                    }
                    annotation = parameter.getAnnotation(Header.class);
                    if (annotation != null) {
                        return new HeaderParam();
                    }
                    annotation = parameter.getAnnotation(RequestAttribute.class);
                    if (annotation != null) {
                        RequestAttribute requestAttribute = (RequestAttribute) annotation;
                        return new RequestAttributeParam(parameter.getName(), requestAttribute.value(),
                                parameter.getType());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public HandlerInvocationResult invoke(Request request) throws Exception {
        try {
            Object[] parameters = getParameters(request);
            method.setAccessible(true);
            Object invocationResult = method.invoke(handlerRootInstance, parameters);

            return new HandlerInvocationResult(responseContentType, invocationResult,
                    method.getReturnType());
        } catch (IllegalAccessException e) {
            throw new HandlerInvocationException("Could not invoke handler method " + method, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Error) {
                throw new HandlerInvocationException("Handler invocation threw error", e);
            } else {
                throw (Exception) e.getCause();
            }
        }
    }

    private Object[] getParameters(Request request) {
        setPathVars(request);
        return paramList.stream()
                .map(param -> param.value(request)).toArray();
    }

    private void setPathVars(Request request) {
        String[] subMappings = path.substring(1).split("/");
        String[] subPaths = request.getUrlPath().substring(1).split("/");
        for (int i = 0; i < subMappings.length; i++) {
            String subMapping = subMappings[i];
            String subPath = subPaths[i];
            if (subMapping.startsWith("{") && subMapping.endsWith("}")) {
                request.setPathVariable(subMapping.substring(1, subMapping.length() - 1), subPath);
            }
        }
    }
}
