package org.crumbs.core.context;

import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.util.ReflectionUtils;

public class CrumbsApp {
    public static CrumbsContext run(Class<?> clazz) {
        if (!ReflectionUtils.hasAnnotation(clazz, CrumbsApplication.class)) {
            throw new RuntimeException("Class is not @CrumbsApplication annotated");
        }
        CrumbsContext context = new CrumbsContext();
        try {
            context.initialize(clazz);
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize Crumbs Context due to exception", e);
        }
        return context;
    }
}
