package test.iface;

import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@CrumbsApplication
public class CrumbsWithInterfaceTest {

    @Crumb
    public static class TestClass {

        @CrumbRef
        private InterfaceAnnotatedWithPlainCrumb crumb;

        public InterfaceAnnotatedWithPlainCrumb getCrumb() {
            return crumb;
        }
    }


    @Test
    public void shouldReturnCrumbsWithAnnotationOnInterface() {
        CrumbsContext context = CrumbsApp.run(CrumbsWithInterfaceTest.class);
        InterfaceAnnotatedWithPlainCrumb crumb = context.getCrumb(InterfaceAnnotatedWithPlainCrumb.class);

        //assertNotNull(crumb);
        //assertEquals("PlainIfaceCrumb", crumb.call());

        TestClass crumb1 = context.getCrumb(TestClass.class);
        assertNotNull(crumb1.getCrumb());
    }

    @Test
    public void shouldReturnCrumbsWithSteretypeAnnotationOnInterface() {
        CrumbsContext context = CrumbsApp.run(CrumbsWithInterfaceTest.class);
        SterotypeIfaceCrumb crumb = context.getCrumb(SterotypeIfaceCrumb.class);

        assertNotNull(crumb);
        assertEquals("StereotypeIfaceCrumb", crumb.call());
    }
}
