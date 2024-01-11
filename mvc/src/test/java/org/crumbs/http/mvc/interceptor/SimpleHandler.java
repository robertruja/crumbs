package org.crumbs.http.mvc.interceptor;

import org.crumbs.http.mvc.annotation.Handler;
import org.crumbs.http.mvc.annotation.HandlerRoot;
import org.crumbs.http.mvc.annotation.RequestAttribute;
import org.crumbs.http.common.model.Mime;

@HandlerRoot("/interceptor/test")
public class SimpleHandler {

    @Handler(mapping = "/some", producesContent = Mime.TEXT_PLAIN)
    public String someHandler(@RequestAttribute String infoFromInterceptor1, @RequestAttribute("infoFromInt") Integer someIntAttr) {
        String info = "" + infoFromInterceptor1 + " and " + someIntAttr;
        return info;
    }

    @Handler(mapping = "/invalid", producesContent = Mime.TEXT_PLAIN)
    public String someInvalid() {
        return "Should not get called";
    }
}
