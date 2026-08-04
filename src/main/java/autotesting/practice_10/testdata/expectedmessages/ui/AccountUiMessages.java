package autotesting.practice_10.testdata.expectedmessages.ui;

public class AccountUiMessages {

    public static final String ACCOUNT_CREATED_SUCCESSFULLY = "✅ New Account Created! Account Number: %s";

    public static final String DEPOSIT_SUCCESSFULLY = "✅ Successfully deposited $%s to account %s!";
    public static final String DEPOSIT_AMOUNT_ABOVE_MAX_LIMIT = "❌ Please deposit less or equal to 5000$.";
    public static final String DEPOSIT_AMOUNT_BELOW_MIN_LIMIT = "❌ Please enter a valid amount.";
    public static final String DEPOSIT_ACCOUNT_NOT_SELECTED = "❌ Please select an account.";

    public static final String TRANSFER_SUCCESSFULLY = "✅ Successfully transferred $%s to account %s!";
    public static final String TRANSFER_REQUIRED_FIELDS_NOT_FILLED = "❌ Please fill all fields and confirm.";
    public static final String TRANSFER_AMOUNT_BELOW_MIN_LIMIT = "❌ Error: Transfer amount must be at least 0.01";

}
