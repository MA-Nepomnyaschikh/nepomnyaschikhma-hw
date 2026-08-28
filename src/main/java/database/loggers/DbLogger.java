package database.loggers;

import database.core.SqlStatement;

import java.sql.SQLException;

public interface DbLogger {

    void logQuery(SqlStatement statement);

    void logResult(Object result);

    void logUpdate(int affectedRows);

    void logError(SqlStatement statement, SQLException exception);
}
