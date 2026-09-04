package database.queries;

import database.core.SqlStatement;

public interface Query {

    SqlStatement buildSql();
}
