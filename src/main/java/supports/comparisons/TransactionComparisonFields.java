package supports.comparisons;

public enum TransactionComparisonFields implements ComparisonFields {

    SELECT_ACCOUNT_TRANSACTIONS_TO_CREATE_ACCOUNT_RESPONSE("id", "amount", "type", "relatedAccountId");

    private final String[] fields;

    TransactionComparisonFields(String... fields) {
        this.fields = fields;
    }

    @Override
    public String[] fields() {
        return fields;
    }
}
