package database.core;

import java.util.List;

public record SqlStatement(String sql, List<Object> parameters) {

}
