package dao;

import dao.loggers.DbLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcExecutor {

    private final ConnectionManager connectionManager;
    private final DbLogger logger;

    public JdbcExecutor(ConnectionManager connectionManager, DbLogger logger) {
        this.connectionManager = connectionManager;
        this.logger = logger;
    }

    public int executeUpdate(SqlStatement statement) {
        logger.logQuery(statement);

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement.sql())) {

            setParameters(preparedStatement, statement.parameters());
            int affectedRows = preparedStatement.executeUpdate();
            logger.logUpdate(affectedRows);
            return affectedRows;

        } catch (SQLException e) {
            logger.logError(statement, e);
            throw new RuntimeException("Failed to execute SQL: " + statement.sql(), e);
        }
    }

    public <T> T executeQuery(SqlStatement statement, ResultSetMapper<T> mapper) {
        logger.logQuery(statement);

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement.sql())) {

            setParameters(preparedStatement, statement.parameters());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                T result = mapper.map(resultSet);
                logger.logResult(result);
                return result;
            }

        } catch (SQLException e) {
            logger.logError(statement, e);
            throw new RuntimeException("Failed to execute SQL: " + statement.sql(), e);
        }
    }

    private void setParameters(PreparedStatement statement, List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            statement.setObject(i + 1, parameters.get(i));
        }
    }

    @FunctionalInterface
    public interface ResultSetMapper<T> {

        T map(ResultSet resultSet) throws SQLException;
    }
}