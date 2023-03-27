package org.crumbs.mvc.context;

import org.crumbs.mvc.context.handler.HandlerParam;
import org.crumbs.mvc.exception.BadRequestException;
import org.crumbs.mvc.exception.HandlerInvocationException;
import org.crumbs.mvc.http.Request;

public class RequestParam implements HandlerParam {
    private String name;
    private String value;
    private Class<?> clazz;
    private boolean required;

    public RequestParam(String value, String name, boolean required, Class<?> clazz) {
        this.name = name;
        this.value = value;
        this.clazz = clazz;
        this.required = required;
    }

    @Override
    public Object value(Request request) {
        String key = value;
        if (value.equals("")) {
            key = name;
        }
        String paramValue = request.getQueryParams().get(key);

        if (required && paramValue == null) {
            throw new BadRequestException("Value is required for request parameter: '" + key + "'");
        }
        if (paramValue == null && clazz.isPrimitive()) {
            throw new HandlerInvocationException("Can not map null param to primitive type: " + key);
        }

        try {
            if (clazz.equals(String.class)) {
                return paramValue;
            } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                return paramValue == null ? null : Integer.parseInt(paramValue);
            } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                return paramValue == null ? null : Boolean.parseBoolean(paramValue);
            } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                return paramValue == null ? null : Double.parseDouble(paramValue);
            } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                return paramValue == null ? null : Float.parseFloat(paramValue);
            } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                return paramValue == null ? null : Long.parseLong(paramValue);
            } else {
                throw new HandlerInvocationException("Unsupported parameter type: " + clazz.getName());
            }
        } catch (NumberFormatException ex) {
            throw new BadRequestException("Invalid value for parameter: '" + key + "'");
        }
    }
}
