package database.conditions;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class InCondition implements Condition {

    private final String column;
    private final List<?> values;

    InCondition(
            String column,
            Collection<?> values
    ) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException(
                    "IN condition requires at least one value"
            );
        }

        this.column = column;
        this.values = List.copyOf(values);
    }

    @Override
    public String buildConditionSql() {
        String placeholders = values.stream()
                .map(value -> "?")
                .collect(Collectors.joining(", "));

        return column + " IN (" + placeholders + ")";
    }

    @Override
    public List<Object> getParameters() {
        return values.stream()
                .map(value -> (Object) value)
                .toList();
    }
}