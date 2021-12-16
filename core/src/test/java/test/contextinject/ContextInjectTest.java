package test.contextinject;

import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

@CrumbsApplication
public class ContextInjectTest {
    @Test
    public void shouldInjectCrumbsContext() {
        CrumbsContext context = CrumbsApp.run(ContextInjectTest.class);
        TestCrumb testCrumb = context.getCrumb(TestCrumb.class);

        assertEquals("called", testCrumb.call());

        CrumbWithContextRef contextRef = context.getCrumb(CrumbWithContextRef.class);

        assertEquals(context, contextRef.getContext());
    }
}
