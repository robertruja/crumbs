package org.crumbs.mvc.context.handler;

public interface PathMatcher {
    boolean matches(String source, String target);
}
