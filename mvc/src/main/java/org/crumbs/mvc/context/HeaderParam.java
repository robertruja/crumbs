package org.crumbs.mvc.context;

import org.crumbs.mvc.context.handler.HandlerParam;
import org.crumbs.mvc.http.Request;

public class HeaderParam implements HandlerParam {
    @Override
    public Object value(Request request) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
