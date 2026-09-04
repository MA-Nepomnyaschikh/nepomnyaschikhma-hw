package database.conditions;

import java.util.List;

class SimpleCondition implements Condition {

    private final String column;
    private final Object value;
    private final String operator;

    SimpleCondition(
            String column,
            Object value,
            String operator
    ) {
        this.column = column;
        this.value = value;
        this.operator = operator;
    }

    @Override
    public String buildConditionSql() {
        return column + " " + operator + " ?";
    }

    @Override
    public List<Object> getParameters() {
        return List.of(value);
    }
}