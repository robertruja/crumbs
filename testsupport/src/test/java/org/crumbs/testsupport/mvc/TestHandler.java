package org.crumbs.testsupport.mvc;

import org.crumbs.http.mvc.annotation.Handler;
import org.crumbs.http.mvc.annotation.HandlerRoot;
import org.crumbs.http.common.model.Mime;

@HandlerRoot("/")
public class TestHandler {
    @Handler(mapping = "/test", producesContent = Mime.TEXT_PLAIN)
    public String test() {
        return "success";
    }
}
