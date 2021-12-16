package org.crumbs.mvc.annotation;

import org.crumbs.core.annotation.Crumb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Crumb
public @interface HandlerRoot {
    String value() default "";
}
