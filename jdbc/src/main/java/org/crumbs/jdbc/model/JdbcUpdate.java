package org.crumbs.jdbc.model;

import java.util.List;

public interface JdbcUpdate {
    String getStatement();
    List<?> getParameters();
}
