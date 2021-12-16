package test.contextinject;

import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.core.context.CrumbsContext;

@Crumb
public class CrumbWithContextRef {

    @CrumbRef
    private CrumbsContext context;

    public CrumbsContext getContext() {
        return context;
    }
}
