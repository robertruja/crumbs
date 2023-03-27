package org.crumbs.mvc.context.handler;

import lombok.Getter;
import org.crumbs.core.annotation.Crumb;
import org.crumbs.mvc.annotation.HandlerRoot;
import org.crumbs.mvc.common.model.HttpMethod;
import org.crumbs.mvc.common.model.Mime;
import org.crumbs.mvc.exception.CrumbsMVCInitException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

                    handler.paramList = Handler.buildParamList(method);
                    rootHandler.addHandler(subPath, handler);
                });
        return rootHandler;
    }


    void addHandler(String path, Handler handler) {

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


    public Handler findHandler(String subPath) {
        return null;
    }
}
