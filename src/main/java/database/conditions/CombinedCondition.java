package database.conditions;

import java.util.ArrayList;
import java.util.List;

class CombinedCondition implements Condition {

    private final Condition left;
    private final Condition right;
    private final String operator;

    CombinedCondition(
            Condition left,
            Condition right,
            String operator
    ) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public String buildConditionSql() {
        return "("
                + left.buildConditionSql()
                + " "
                + operator
                + " "
                + right.buildConditionSql()
                + ")";
    }

    @Override
    public List<Object> getParameters() {
        List<Object> parameters = new ArrayList<>();

        parameters.addAll(left.getParameters());
        parameters.addAll(right.getParameters());

        return parameters;
    }
}