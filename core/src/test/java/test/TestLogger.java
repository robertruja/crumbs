package test;

import org.crumbs.core.annotation.CrumbsApplication;
import org.crumbs.core.context.CrumbsApp;
import org.crumbs.core.context.CrumbsContext;
import org.crumbs.core.logging.Logger;
import org.junit.Test;

@CrumbsApplication
public class TestLogger {

    @Test
    public void shouldLogEveryClassByDefault() {
        CrumbsContext context = CrumbsApp.run(TestLogger.class);
        Logger logger = Logger.getLogger(TestLogger.class);
        logger.info("Logging this");
    }

    @Test
    public void shouldLogOnlyDeclaredClassesInPackage() {
        System.setProperty("log.packages", "org.crumbs");
        CrumbsContext context = CrumbsApp.run(TestLogger.class);
        Logger logger = Logger.getLogger(TestLogger.class);
        logger.info("Logging this");
    }
}
