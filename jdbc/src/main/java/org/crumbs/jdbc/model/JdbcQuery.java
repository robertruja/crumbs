package org.crumbs.jdbc.model;

import java.util.List;

public interface JdbcQuery<T> {
    String getSql();

    List<?> getParameters();

    Class<T> getResultSetType();
}
