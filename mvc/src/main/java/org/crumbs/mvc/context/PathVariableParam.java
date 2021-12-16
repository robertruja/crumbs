package org.crumbs.mvc.context;

import org.crumbs.mvc.exception.CrumbsMVCInitException;
import org.crumbs.mvc.http.Request;

public class PathVariableParam implements HandlerParam {

    private String name;
    private String value;
    private Class<?> clazz;

    public PathVariableParam(String name, String value, Class<?> clazz) {
        this.name = name;
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public Object value(Request request) {
        String key = value;
        if(value.equals("")) {
            key = name;
        }
        String paramValue = request.getPathVarialbe(key);
        if(paramValue != null && !paramValue.isEmpty()) {
            if(clazz.equals(String.class)) {
                return paramValue;
            } else if(clazz.equals(Integer.class)) {
                return Integer.parseInt(paramValue);
            } else if(clazz.equals(Boolean.class)) {
                return Boolean.parseBoolean(paramValue);
            } else if(clazz.equals(Double.class)) {
                return Double.parseDouble(paramValue);
            } else if(clazz.equals(Float.class)) {
                return Float.parseFloat(paramValue);
            } else if(clazz.equals(Long.class)) {
                return Long.parseLong(paramValue);
            } else {
                throw new CrumbsMVCInitException("Unsupported parameter type: " + clazz.getName());
            }
        }
        return null;
    }
}
