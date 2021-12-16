package test.contextinject;

import org.crumbs.core.annotation.Crumb;

@Crumb
public class TestCrumb {
    public String call() {
        return "called";
    }
}
