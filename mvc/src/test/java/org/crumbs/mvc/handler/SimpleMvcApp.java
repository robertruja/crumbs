package org.crumbs.mvc.handler;

import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;

@CrumbsApplication
public class SimpleMvcApp {
    public static void main(String[] args) {
        CrumbsApp.run(SimpleMvcApp.class);
    }
}
