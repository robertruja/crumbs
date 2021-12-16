package org.crumbs.jdbc.manager;

import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.jdbc.connector.JdbcConnector;
import org.crumbs.jdbc.model.BatchUpdateParameters;
import org.crumbs.jdbc.model.JdbcBatchUpdate;
import org.crumbs.jdbc.model.JdbcQuery;
import org.crumbs.jdbc.model.JdbcUpdate;
import org.crumbs.jdbc.reflection.SqlType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Crumb
public class JDBCManager {

    @CrumbRef
    private JdbcConnector connector;

    public <T> List<T> executeQuery(JdbcQuery<T> query) {

        String sql = query.getSql();
        List<?> parameters = query.getParameters();
        Class<T> rsType = query.getResultSetType();

        try (Connection connection = connector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            mapParameters(stmt, parameters);
            ResultSet rs = stmt.executeQuery();
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(mapRow(rsType, rs));
            }
            return results;
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to execute query due to sql exception", ex);
        }
    }

    public int executeUpdate(JdbcUpdate jdbcUpdate) {
        try (Connection connection = connector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(jdbcUpdate.getStatement());
            mapParameters(stmt, jdbcUpdate.getParameters());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to execute update due to sql exception", ex);
        }
    }

    public void batchInsert(JdbcBatchUpdate jdbcBatchUpdate) {
        try (Connection connection = connector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(jdbcBatchUpdate.getSql());
            List<BatchUpdateParameters> parameters = jdbcBatchUpdate.getJdbcBatchParameters();
            for (BatchUpdateParameters batchUpdateParameters : parameters) {
                mapParameters(stmt, batchUpdateParameters.getParameters());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to execute update due to sql exception", ex);
        }
    }

    private void mapParameters(PreparedStatement stmt, List<?> parameters) throws SQLException {
        int i = 0;
        for (Object param : parameters) {
            switch (SqlType.fromClass(param.getClass())) {
                case BOOLEAN:
                    stmt.setBoolean(++i, (boolean) param); break;
                case LONG:
                    stmt.setLong(++i, (long) param); break;
                case FLOAT:
                    stmt.setFloat(++i, (float) param); break;
                case DOUBLE:
                    stmt.setDouble(++i, (double) param); break;
                case INTEGER:
                    stmt.setInt(++i, (int) param); break;
                case STRING:
                    stmt.setString(++i, (String) param); break;
                case TIMESTAMP:
                    stmt.setTimestamp(++i, (Timestamp) param); break;
            }
        }
    }

    private <T> T mapRow(Class<T> type, ResultSet rs) throws SQLException {
        try {
            T instance = type.getConstructor().newInstance();
            int i = 0;
            Field[] fields = type.getDeclaredFields();
            for(Field field: fields) {
                field.setAccessible(true);
                switch (SqlType.fromClass(field.getType())) {
                    case BOOLEAN:
                        field.setBoolean(instance, rs.getBoolean(++i));
                        break;
                    case STRING:
                        field.set(instance, rs.getString(++i));
                        break;
                    case INTEGER:
                        field.setInt(instance, rs.getInt(++i));
                        break;
                    case DOUBLE:
                        field.setDouble(instance, rs.getDouble(++i));
                        break;
                    case FLOAT:
                        field.setFloat(instance, rs.getFloat(++i));
                        break;
                    case LONG:
                        field.setLong(instance, rs.getLong(++i));
                        break;
                    case TIMESTAMP:
                        field.set(instance, rs.getTimestamp(++i));
                        break;
                }
                field.setAccessible(false);
            }
            return instance;
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
            throw new RuntimeException("Could not map row due to reflection error", e);
        }
    }
}
