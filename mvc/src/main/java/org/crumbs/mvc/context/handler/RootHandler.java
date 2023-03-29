package org.crumbs.mvc.context.handler;

import lombok.Getter;
import org.crumbs.core.annotation.Crumb;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.exception.CrumbsMVCInitException;

import java.util.Arrays;


@Crumb
@Getter
public class RootHandler {

    private String rootPath;
    private HandlerTreeNode root = new HandlerTreeNode();

    private RootHandler() {}

    static RootHandler fromRootHandlerCrumb(Object handlerCrumb) {

        Class<?> clazz = handlerCrumb.getClass();

        String rootPath = clazz.getAnnotation(HandlerRoot.class).value();

        if (!rootPath.startsWith("/")) {
            throw new CrumbsMVCInitException("Invalid handler path: " + rootPath + ". Must start with '/'");
        }
        RootHandler rootHandler = new RootHandler();
        rootHandler.rootPath = rootPath;
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getAnnotation(org.crumbs.mvc.annotation.Handler.class) != null)
                .forEach(method -> {
                    org.crumbs.mvc.annotation.Handler annotation =
                            method.getAnnotation(org.crumbs.mvc.annotation.Handler.class);

                    String subPath = annotation.mapping();
                    HttpMethod httpMethod = annotation.method();
                    Mime mime = annotation.producesContent();
                    Handler handler = new Handler();
                    handler.handlerRootInstance = handlerCrumb;
                    handler.httpMethod = httpMethod;
                    handler.responseContentType = mime;
                    handler.method = method;
                    handler.path = subPath;
                    handler.paramList = Handler.buildParamList(method);
                    rootHandler.addHandler(subPath, handler);
                });
        return rootHandler;
    }


    private void addHandler(String path, Handler handler) {

        HandlerTreeNode source = pathToNode(path);
        PathMatcher matcher = new AbsolutePathMatcher();
        Handler existing = findRecursively(source, root, matcher);
        if(existing != null) {
            throw new RuntimeException("Conflict: Path " + path + " is already defined with: " + existing.getPath());
        }
        source.setHandler(handler);
        addRecursively(source, root, matcher);
    }

    Handler findHandler(String path) {
        HandlerTreeNode source = pathToNode(path);
        return findRecursively(source, root, new PathVarMatcher());
    }

    private Handler findRecursively(HandlerTreeNode source, HandlerTreeNode currentExisting, PathMatcher matcher) {
        if(matcher.matches(source.getPath(), currentExisting.getPath())) {
            if(!source.hasMoreChildren()) {
                return currentExisting.getHandler();
            } else {
                HandlerTreeNode srcChild = source.getChildren().get(0);
                Handler result = null;
                for(HandlerTreeNode existingChildOfCurrent: currentExisting.getChildren()) {
                    result = findRecursively(srcChild, existingChildOfCurrent, matcher);
                    if(result != null) {
                        break;
                    }
                }
                return result;
            }
        }
        return null;
    }

    private boolean addRecursively(HandlerTreeNode source, HandlerTreeNode currentExisting, PathMatcher matcher) {
        boolean added = false;
        if(matcher.matches(source.getPath(), currentExisting.getPath())) {
            if(source.hasMoreChildren()) {
                HandlerTreeNode srcChild = source.getChildren().get(0);
                for(HandlerTreeNode existingChildOfCurrent: currentExisting.getChildren()) {
                    added = addRecursively(srcChild, existingChildOfCurrent, matcher);
                    if(added) {
                        break;
                    }
                }
                if(!added) {
                    currentExisting.addChild(srcChild);
                    added = true;
                }
            } else {
                currentExisting.setHandler(source.getHandler());
                added = true;
            }
        }
        return added;
    }

    private HandlerTreeNode pathToNode(String path) {
        if(path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if(!path.startsWith("/")) {
            throw new RuntimeException("The path " + path + " does not start with '/'");
        }
        String[] subPaths = path.substring(1).split("/"); // ignore first '/'

        HandlerTreeNode parent = new HandlerTreeNode();
        HandlerTreeNode current = parent;

        for(int i = 0; i < subPaths.length; i++) {
            current.setPath("/" + subPaths[i]);
            if(i < subPaths.length - 1) {
                HandlerTreeNode node = new HandlerTreeNode();
                current.addChild(node);
                current = node;
            }
        }
        return parent;
    }
}
