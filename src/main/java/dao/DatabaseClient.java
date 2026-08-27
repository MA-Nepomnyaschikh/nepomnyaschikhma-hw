package dao;

import dao.loggers.DbLogger;
import dao.queries.SelectQuery;

public class DatabaseClient {

    private final JdbcExecutor jdbcExecutor;

    public DatabaseClient(ConnectionManager connectionManager, DbLogger logger) {
        this.jdbcExecutor = new JdbcExecutor(connectionManager, logger);
    }

    public SelectQuery.SelectQueryBuilder select(String... columns) {
        return new SelectQuery.SelectQueryBuilder(jdbcExecutor, columns);
    }
}