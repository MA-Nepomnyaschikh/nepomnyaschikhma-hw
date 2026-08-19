package dao.queries;

import dao.JdbcExecutor;
import dao.ObjectMapper;
import dao.SqlStatement;
import dao.conditions.Condition;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SelectQuery implements Query {

    private final JdbcExecutor jdbcExecutor;
    private final List<String> columns;
    private final String table;
    private final Condition condition;
    private final String orderBy;
    private final Integer limit;

    private SelectQuery(
            JdbcExecutor jdbcExecutor,
            List<String> columns,
            String table,
            Condition condition,
            String orderBy,
            Integer limit
    ) {
        this.jdbcExecutor = jdbcExecutor;
        this.columns = columns;
        this.table = table;
        this.condition = condition;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    @Override
    public SqlStatement buildSql() {
        StringBuilder sql = new StringBuilder();
        List<Object> parameters = new ArrayList<>();

        sql.append("SELECT ")
                .append(String.join(", ", columns))
                .append(" FROM ")
                .append(table);

        if (condition != null) {
            sql.append(" WHERE ")
                    .append(condition.buildConditionSql());

            parameters.addAll(condition.getParameters());
        }

        if (orderBy != null) {
            sql.append(" ORDER BY ")
                    .append(orderBy);
        }

        if (limit != null) {
            sql.append(" LIMIT ")
                    .append(limit);
        }

        return new SqlStatement(
                sql.toString(),
                parameters
        );
    }

    private <T> T execute(JdbcExecutor.ResultSetMapper<T> mapper) {
        return jdbcExecutor.executeQuery(buildSql(), mapper);
    }

    public <T> List<T> executeAndGetList(Class<T> type) {
        return execute((ResultSet resultSet) -> {
            ObjectMapper<T> mapper = new ObjectMapper<>(type);
            List<T> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(mapper.map(resultSet));
            }

            return result;
        });
    }

    public <T> T executeAndGet(Class<T> type) {
        return jdbcExecutor.executeQuery(buildSql(), resultSet -> {
            ObjectMapper<T> mapper = new ObjectMapper<>(type);

            if (resultSet.next()) {
                return mapper.map(resultSet);
            }

            return null;
        });
    }

    public static class SelectQueryBuilder {

        private final JdbcExecutor jdbcExecutor;
        private final List<String> columns;
        private String table;
        private Condition condition;
        private String orderBy;
        private Integer limit;

        public SelectQueryBuilder(JdbcExecutor jdbcExecutor, String... columns) {
            this.jdbcExecutor = jdbcExecutor;
            this.columns = List.of(columns);
        }

        public SelectQueryBuilder from(String table) {
            this.table = table;
            return this;
        }

        public SelectQueryBuilder where(Condition condition) {
            this.condition = condition;
            return this;
        }

        public SelectQueryBuilder orderBy(String column) {
            this.orderBy = column;
            return this;
        }

        public SelectQueryBuilder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public SelectQuery build() {
            return new SelectQuery(
                    jdbcExecutor,
                    columns,
                    table,
                    condition,
                    orderBy,
                    limit
            );
        }
    }
}