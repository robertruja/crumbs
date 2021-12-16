package org.crumbs.mvc.interceptor;

import org.crumbs.mvc.annotation.Handler;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.annotation.RequestAttribute;
import org.crumbs.mvc.common.model.Mime;

@HandlerRoot("/interceptor/test")
public class SimpleHandler {

    @Handler(value = "/some", producesContent = Mime.TEXT_PLAIN)
    public String someHandler(@RequestAttribute String infoFromInterceptor1, @RequestAttribute("infoFromInt") Integer someIntAttr) {
        String info = "" + infoFromInterceptor1 + " and " + someIntAttr;
        return info;
    }

    @Handler(value = "/invalid", producesContent = Mime.TEXT_PLAIN)
    public String someInvalid() {
        return "Should not get called";
    }
}
