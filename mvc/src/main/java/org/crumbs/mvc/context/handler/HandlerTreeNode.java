package org.crumbs.mvc.context.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HandlerTreeNode {

    private Map<String, HandlerTreeNode> children = new LinkedList<>();
    private Handler handler;

    private String path;

    void addChild(HandlerTreeNode child) {
        children.add(child);
    }

    void setPath(String path) {
        this.path = path;
    }
}
