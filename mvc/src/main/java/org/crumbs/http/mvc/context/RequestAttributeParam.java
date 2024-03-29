package org.crumbs.http.mvc.context;

import org.crumbs.http.mvc.context.handler.HandlerParam;
import org.crumbs.http.mvc.exception.HandlerInvocationException;
import org.crumbs.http.mvc.http.Request;

public class RequestAttributeParam implements HandlerParam {
    private String name;
    private String value;
    private Class<?> clazz;

    public RequestAttributeParam(String name, String value, Class<?> clazz) {
        this.name = name;
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public Object value(Request request) {
        String key = value;
        if (value.equals("")) {
            key = name;
        }

        Object attribute = request.getAttribute(key);

        if (attribute == null && clazz.isPrimitive()) {
            throw new HandlerInvocationException("Can not map null attribute to parameter type: " + clazz.getName());
        }
        if (attribute != null && !attribute.getClass().equals(clazz)) {
            // cover primitive types
            if (!attribute.getClass().getSimpleName().equalsIgnoreCase(clazz.getSimpleName())) {
                throw new HandlerInvocationException("Can not map type " + attribute.getClass().getName() +
                        " to parameter type " + clazz.getName());
            }
        }
        return attribute;
    }
}
