package org.crumbs.mvc.context.handler;

import java.util.LinkedList;
import java.util.List;

public class HandlerTreeNode {

    private List<HandlerTreeNode> children = new LinkedList<>();
    private Handler handler;

    private String path;

    void addChild(HandlerTreeNode child) {
        children.add(child);
    }

    void setPath(String path) {
        this.path = path;
    }

    public boolean hasMoreChildren() {
        return children.size() > 0;
    }

    public Handler getHandler() {
        return handler;
    }

    public List<HandlerTreeNode> getChildren() {
        return children;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public String getPath() {
        return path;
    }
}
