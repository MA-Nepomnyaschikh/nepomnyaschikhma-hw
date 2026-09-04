package database.conditions;

import java.util.Collection;
import java.util.List;

public interface Condition {

    String buildConditionSql();

    List<Object> getParameters();

    default Condition and(Condition other) {
        return new CombinedCondition(this, other, "AND");
    }

    default Condition or(Condition other) {
        return new CombinedCondition(this, other, "OR");
    }

    static Condition eq(String column, Object value) {
        return new SimpleCondition(column, value, "=");
    }

    static Condition ne(String column, Object value) {
        return new SimpleCondition(column, value, "<>");
    }

    static Condition gt(String column, Object value) {
        return new SimpleCondition(column, value, ">");
    }

    static Condition ge(String column, Object value) {
        return new SimpleCondition(column, value, ">=");
    }

    static Condition lt(String column, Object value) {
        return new SimpleCondition(column, value, "<");
    }

    static Condition le(String column, Object value) {
        return new SimpleCondition(column, value, "<=");
    }

    static Condition like(String column, Object value) {
        return new SimpleCondition(column, value, "LIKE");
    }

    static Condition in(String column, Collection<?> values) {
        return new InCondition(column, values);
    }
}