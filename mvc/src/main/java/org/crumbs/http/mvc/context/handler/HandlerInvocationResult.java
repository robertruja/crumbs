package org.crumbs.http.mvc.context.handler;

import org.crumbs.http.common.model.Mime;

public class HandlerInvocationResult {

    private Mime mime;
    private Object content;
    private Class<?> returnType;

    public HandlerInvocationResult(Mime mime, Object content, Class<?> returnType) {
        this.mime = mime;
        this.content = content;
        this.returnType = returnType;
    }

    public Mime getMime() {
        return mime;
    }

    public Object getContent() {
        return content;
    }

    public Class<?> getReturnType() {
        return returnType;
    }
}
