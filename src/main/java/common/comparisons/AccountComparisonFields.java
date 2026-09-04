package common.comparisons;

public enum AccountComparisonFields implements ComparisonFields {

    SELECT_ACCOUNT_RESPONSE_TO_CREATE_ACCOUNT_RESPONSE("id","accountNumber", "balance");

    private final String[] fields;

    AccountComparisonFields(String... fields) {
        this.fields = fields;
    }
    
    @Override
    public String[] fields() {
        return fields;
    }
}
