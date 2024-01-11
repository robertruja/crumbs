package org.crumbs.http.mvc.annotation;

import org.crumbs.http.common.model.HttpMethod;
import org.crumbs.http.common.model.Mime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {
    String mapping() default "/";

    HttpMethod method() default HttpMethod.GET;

    Mime producesContent() default Mime.TEXT_PLAIN;
}
