package supports.comparisons;

public enum UserComparisonFields implements ComparisonFields {

    CREATE_USER_REQUEST_TO_CREATE_USER_RESPONSE("username","role"),
    CREATE_USER_REQUEST_TO_GET_USER_RESPONSE("username","role"),
    GET_USER_RESPONSE_TO_CREATE_USER_RESPONSE("id", "username", "name", "role", "accounts"),
    SELECT_USER_RESPONSE_TO_CREATE_USER_RESPONSE("id", "username", "password", "name", "role"),
    SELECT_USER_RESPONSE_TO_GET_USER_RESPONSE("id", "username", "name", "role"),
    GET_USER_PROFILE_RESPONSE_TO_CREATE_USER_RESPONSE("id", "username", "name", "role"),

    LOGIN_USER_RESPONSE_TO_CREATE_USER_REQUEST("username","role");

    private final String[] fields;

    UserComparisonFields(String... fields) {
        this.fields = fields;
    }

    @Override
    public String[] fields() {
        return fields;
    }
}
