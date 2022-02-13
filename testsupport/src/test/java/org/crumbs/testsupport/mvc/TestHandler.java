package org.crumbs.testsupport.mvc;

import org.crumbs.mvc.annotation.Handler;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.common.model.Mime;

@HandlerRoot
public class TestHandler {
    @Handler(mapping = "/test", producesContent = Mime.TEXT_PLAIN)
    public String test() {
        return "success";
    }
}
