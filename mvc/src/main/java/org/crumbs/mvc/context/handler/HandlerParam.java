package org.crumbs.mvc.context.handler;

import org.crumbs.mvc.http.Request;

public interface HandlerParam {
    Object value(Request request);
}
