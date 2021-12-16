package org.crumbs.jdbc.model;

import java.util.List;

public interface JdbcBatchUpdate {
    String getSql();
    List<BatchUpdateParameters> getJdbcBatchParameters();
}
