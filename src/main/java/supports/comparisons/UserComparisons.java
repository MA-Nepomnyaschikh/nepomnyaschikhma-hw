package supports.comparisons;

public enum UserComparisons implements Comparisons {

    CREATE_USER(
            "username",
                    "role"
    ),

    LOGIN_USER(
            "username",
                    "role"
    );

    private final String[] fields;


    UserComparisons(String... fields) {
        this.fields = fields;
    }


    @Override
    public String[] fields() {
        return fields;
    }
}
