package test.annotation;

import org.crumbs.core.annotation.Crumb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Crumb
public @interface TestCustomAnnotation {
}
