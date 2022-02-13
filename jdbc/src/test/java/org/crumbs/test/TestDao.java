package org.crumbs.test;

import org.crumbs.core.annotation.Crumb;
import org.crumbs.core.annotation.CrumbInit;
import org.crumbs.core.annotation.CrumbRef;
import org.crumbs.jdbc.manager.JDBCManager;
import org.crumbs.jdbc.manager.Jdbc;
import org.crumbs.jdbc.model.BatchUpdateParameters;
import org.crumbs.jdbc.model.JdbcQuery;
import org.crumbs.jdbc.model.JdbcUpdate;

import java.util.List;
import java.util.stream.Collectors;

@Crumb
public class TestDao {

    private static final String RETRIEVE_SQL = "SELECT * FROM users";
    private static final String INSERT_SQL = "INSERT INTO users VALUES(?,?,?,?) ON CONFLICT(id) DO UPDATE SET name = ?, age = ?, event_time = ?";
    private static final String UPDATE_SQL = "UPDATE users SET name=? WHERE name=?";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id=?";

    private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS users ("
            + "	id integer PRIMARY KEY,"
            + "	name text NOT NULL,"
            + " age integer NOT NULL,"
            + "	event_time real"
            + ");";

    @CrumbRef
    private JDBCManager manager;

    @CrumbInit
    private void createTableIfNotExists() {
        System.out.println("Creating table");
        manager.executeUpdate(Jdbc.update(CREATE_TABLE_IF_NOT_EXISTS).build());
    }

    public List<RowModel> retrieveRowModels(int maxAge) {
        JdbcQuery<RowModel> query =
                Jdbc.query(RETRIEVE_SQL, RowModel.class)
                        .build();
        return manager.executeQuery(query);
    }

    public void insertRow(RowModel rowModel) {
        JdbcUpdate update = Jdbc.update(INSERT_SQL)
                .nextParam(rowModel.getId())
                .nextParam(rowModel.getName())
                .nextParam(rowModel.getAge())
                .nextParam(rowModel.getEventTime())
                .nextParam(rowModel.getName())
                .nextParam(rowModel.getAge())
                .nextParam(rowModel.getEventTime())
                .build();

        manager.executeUpdate(update);
    }

    public void insertBatch(List<RowModel> rowModelList) {

        manager.batchInsert(Jdbc.batchUpdate(INSERT_SQL)
                .params(rowModelList.stream().map(rowModel ->
                        BatchUpdateParameters.from(
                                rowModel.getId(),
                                rowModel.getName(),
                                rowModel.getAge(),
                                rowModel.getEventTime(),
                                rowModel.getName(),
                                rowModel.getAge(),
                                rowModel.getEventTime()
                        )).collect(Collectors.toList()))
                .build()
        );
    }

    public List<String> retrieveListOfColumns() {
        return manager.executeQuery(Jdbc.query("select name from users", String.class).build());
    }
}
