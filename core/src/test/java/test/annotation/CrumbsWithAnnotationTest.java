package test.annotation;

import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@CrumbsApplication
public class CrumbsWithAnnotationTest {

    @Test
    public void shouldReturnCrumbsWithAnnotation() {
        CrumbsContext context = CrumbsApp.run(CrumbsWithAnnotationTest.class);
        List<?> crumbs = context.getCrumbsWithAnnotation(TestCustomAnnotation.class);

        assertEquals(2, crumbs.size());
        assertTrue(crumbs.stream().anyMatch(crumb -> crumb.getClass().equals(CustomAnnotationCrumb.class)));
        assertTrue(crumbs.stream().anyMatch(crumb -> crumb.getClass().equals(CustomAnnotationCrumb1.class)));
    }
}
