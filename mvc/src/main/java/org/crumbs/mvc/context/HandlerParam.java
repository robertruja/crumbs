package org.crumbs.mvc.context;

import org.crumbs.mvc.http.Request;

public interface HandlerParam {
    Object value(Request request);
}
