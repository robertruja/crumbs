package org.crumbs.mvc.context;

import org.crumbs.json.JsonMapper;
import org.crumbs.json.exception.JsonUnmarshalException;
import org.crumbs.mvc.exception.BadRequestException;
import org.crumbs.mvc.http.Request;

public class RequestBodyParam implements HandlerParam {
    private final Class<?> type;

    public RequestBodyParam(Class<?> type) {
        this.type = type;
    }

    @Override
    public Object value(Request request) {
        if (type.equals(Request.class)) {
            return request;
        }
        try {
            return new JsonMapper().unmarshal(request.getBody(), type);
        } catch (JsonUnmarshalException e) {
            throw new BadRequestException("Could not deserialize request body of type " + type.getName(), e);
        }
    }
}
