package org.crumbs.testsupport;

import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.crumbs.http.mvc.http.impl.sun.Server;
import org.crumbs.testsupport.exception.CrumbsJunitExtensionException;
import org.crumbs.testsupport.mvc.MvcTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

public class CrumbsExtension implements TestInstanceFactory, ExtensionContext.Store.CloseableResource {

    private static CrumbsContext crumbsContext;
    private static Server server;

    static {
        System.setProperty("crumbs.mvc.server.enabled", "false");
    }

    @Override
    public Object createTestInstance(TestInstanceFactoryContext testInstanceFactoryContext,
                                     ExtensionContext extensionContext) throws TestInstantiationException {
        Class<?> clazz = testInstanceFactoryContext.getTestClass();

        if (crumbsContext == null) {
            crumbsContext = CrumbsApp.run(clazz);
        }

        MvcTest mvc = clazz.getAnnotation(MvcTest.class);
        if (mvc != null) {
            server = crumbsContext.getCrumb(Server.class);
            if (server == null) {
                throw new CrumbsJunitExtensionException("Could not find crumbs instance for class: " + Server.class);
            }
            server.setServerEnabled(true);
            server.setPort(mvc.port());
            server.start();
        }

        Object instance = crumbsContext.getCrumb(clazz);
        if (instance == null) {
            throw new CrumbsJunitExtensionException("Could not find crumbs instance for class: " + clazz.getName());
        }
        return instance;
    }

    @Override
    public void close() throws Throwable {
        server.stop();
    }
}
