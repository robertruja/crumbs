package org.crumbs.core.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Crumb
public @interface CrumbsApplication {
}
