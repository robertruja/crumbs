package org.crumbs.http.mvc.context;

import org.crumbs.http.mvc.context.handler.HandlerParam;
import org.crumbs.http.mvc.http.Request;

public class HeaderParam implements HandlerParam {
    @Override
    public Object value(Request request) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
