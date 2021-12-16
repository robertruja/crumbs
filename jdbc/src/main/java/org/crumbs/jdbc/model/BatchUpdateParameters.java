package org.crumbs.jdbc.model;

import java.util.Arrays;
import java.util.List;

public class BatchUpdateParameters {
    private BatchUpdateParameters() {}
    private List<?> parameters;

    public static BatchUpdateParameters from(Object... params) {
        BatchUpdateParameters parameters = new BatchUpdateParameters();
        parameters.parameters = Arrays.asList(params);
        return parameters;
    }

    public List<?> getParameters() {
        return parameters;
    }
}
