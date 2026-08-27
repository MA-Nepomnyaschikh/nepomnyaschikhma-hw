package dao.queries;

import dao.SqlStatement;

public interface Query {

    SqlStatement buildSql();
}
