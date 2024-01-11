package org.crumbs.http.mvc.context.handler;

import org.crumbs.http.mvc.exception.ConflictException;
import org.crumbs.http.common.model.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HandlerTreeNode {

    private static final Pattern PATH_VAR_PATTERN = Pattern.compile("/\\{[a-zA-Z0-9_\\-]*\\}");
    private static final Pattern SUB_PATH_PATTERN = Pattern.compile("/[a-zA-Z0-9_\\-]*");

    private Map<String, HandlerTreeNode> absSubmappings = new HashMap<>();
    private HandlerTreeNode wildcardSubmapping;
    private Map<HttpMethod, Handler> handlerMap = new HashMap<>();

    public HandlerTreeNode getChild(String currentPath) {
        HandlerTreeNode child = absSubmappings.get(currentPath);
        if (child == null) {
            child = wildcardSubmapping;
        }
        return child;
    }

    public Map<HttpMethod, Handler> getHandlers() {
        return handlerMap;
    }

    public void addSubmapping(String path, HandlerTreeNode node) {
        if (PATH_VAR_PATTERN.matcher(path).matches()) {
            if (wildcardSubmapping != null) {
                throw new ConflictException(path + " is conflicting with and already defined path var entry");
            }
            wildcardSubmapping = node;
        } else if (SUB_PATH_PATTERN.matcher(path).matches()) {
            absSubmappings.put(path, node);
        } else {
            throw new IllegalArgumentException("Invalid sub path format: " + path);
        }
    }

    public void putHandler(HttpMethod method, Handler handler) {
        handlerMap.put(method, handler);
    }
}
