package org.crumbs.testsupport.annotation;

import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.testsupport.CrumbsExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@CrumbsApplication
@ExtendWith(CrumbsExtension.class)
public @interface CrumbsTest {
}
