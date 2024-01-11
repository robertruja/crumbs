package org.crumbs.http.mvc.context.handler;

import org.crumbs.http.mvc.http.Request;

public interface HandlerParam {
    Object value(Request request);
}
