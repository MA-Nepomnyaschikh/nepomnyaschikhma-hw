package dao.loggers;

import dao.SqlStatement;

import java.sql.SQLException;

public class ConsoleDbLogger implements DbLogger {

    @Override
    public void logQuery(SqlStatement statement) {
        System.out.println("[DB] SQL: " + statement.sql());
        System.out.println("[DB] Parameters: " + statement.parameters());
    }

    @Override
    public void logResult(Object result) {
        System.out.println("[DB] Result: " + result);
    }

    @Override
    public void logUpdate(int affectedRows) {
        System.out.println("[DB] Affected rows: " + affectedRows);
    }

    @Override
    public void logError(SqlStatement statement, SQLException exception) {
        System.out.println("[DB] Failed to execute SQL: " + statement.sql());
        System.out.println("[DB] Error: " + exception.getMessage());
    }
}
