package org.crumbs.testsupport;

import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.testsupport.annotation.CrumbsTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@CrumbsTest
public class TestCrumbsExtension {

    @CrumbRef
    private TestCrumbsClass theClass;

    @Test
    public void shouldSuccessfullyCall() {
        Assertions.assertEquals("success", theClass.call());
    }
}
