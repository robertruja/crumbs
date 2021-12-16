package org.crumbs.jdbc.manager;

import org.crumbs.jdbc.model.BatchUpdateParameters;
import org.crumbs.jdbc.model.JdbcBatchUpdate;
import org.crumbs.jdbc.model.JdbcQuery;
import org.crumbs.jdbc.model.JdbcUpdate;

import java.util.ArrayList;
import java.util.List;

public class Jdbc {
    private Jdbc(){}

    public static <T> JdbcQueryBuilder<T> query(String queryString, Class<T> clazz) {
        return new JdbcQueryBuilder<>(queryString, clazz);
    }

    public static JdbcUpdateBuilder update(String updateString) {
        return new JdbcUpdateBuilder(updateString);
    }

    public static JdbcBatchUpdateBuilder batchUpdate(String updateString) {
        return new JdbcBatchUpdateBuilder(updateString);
    }

    static class Query<T> implements JdbcQuery<T> {

        private String queryString;
        private List<Object> parameters;
        private Class<T> rsType;

        private Query(){}

        @Override
        public String getSql() {
            return queryString;
        }

        @Override
        public List<?> getParameters() {
            return parameters;
        }

        @Override
        public Class<T> getResultSetType() {
            return rsType;
        }
    }

    static class Update implements JdbcUpdate {

        private String updateString;
        private List<Object> parameters;

        private Update(){}

        @Override
        public String getStatement() {
            return updateString;
        }

        @Override
        public List<?> getParameters() {
            return parameters;
        }
    }

    static class BatchUpdate implements JdbcBatchUpdate {

        private String updateString;
        private List<BatchUpdateParameters> batchParameters;

        @Override
        public String getSql() {
            return updateString;
        }

        @Override
        public List<BatchUpdateParameters> getJdbcBatchParameters() {
            return batchParameters;
        }
    }

    public static class JdbcQueryBuilder<T> {
        private Query<T> query;
        private JdbcQueryBuilder(String queryString, Class<T> clazz){
            this.query = new Query<>();
            query.parameters = new ArrayList<>();
            query.rsType = clazz;
            query.queryString = queryString;
        }

        public JdbcQueryBuilder<T> nextParam(Object param) {
            query.parameters.add(param);
            return this;
        }

        public JdbcQuery<T> build() {
            return query;
        }
    }

    public static class JdbcUpdateBuilder {
        private Update update;
        private JdbcUpdateBuilder(String updateString){
            this.update = new Update();
            update.updateString = updateString;
            update.parameters = new ArrayList<>();
        }
        public JdbcUpdateBuilder nextParam(Object param) {
            update.parameters.add(param);
            return this;
        }

        public JdbcUpdate build() {
            return update;
        }
    }

    public static class JdbcBatchUpdateBuilder {
        private BatchUpdate batchUpdate;
        private JdbcBatchUpdateBuilder(String updateString) {
            this.batchUpdate = new BatchUpdate();
            batchUpdate.updateString = updateString;
            batchUpdate.batchParameters = new ArrayList<>();
        }

        public JdbcBatchUpdateBuilder params(List<BatchUpdateParameters> parameters) {
            batchUpdate.batchParameters = parameters;
            return this;
        }

        public JdbcBatchUpdate build() {
            return batchUpdate;
        }
    }
}
