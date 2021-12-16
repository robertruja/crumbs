package org.crumbs.mvc.context;

import org.crumbs.mvc.common.model.Mime;

public class HandlerInvocationResult {

    public HandlerInvocationResult(Mime mime, Object content, Class<?> returnType) {
        this.mime = mime;
        this.content = content;
        this.returnType = returnType;
    }

    private Mime mime;
    private Object content;
    private Class<?> returnType;

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
