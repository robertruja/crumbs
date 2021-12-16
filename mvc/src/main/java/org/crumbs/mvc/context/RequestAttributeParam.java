package org.crumbs.mvc.context;

import org.crumbs.mvc.http.Request;

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
        if(value.equals("")) {
            key = name;
        }
        return request.getAttribute(key);
    }
}
