package org.crumbs.testsupport.mvc;

import org.crumbs.testsupport.annotation.CrumbsTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@CrumbsTest
public @interface MvcTest {
    int port();
}
