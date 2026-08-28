package common.testdata.messages.api;

public class AccountApiMessages {

    public static final String DEPOSIT_UNAUTHORIZED = "Unauthorized access to account";

    public static final String TRANSFER_SUCCESSFUL = "Transfer successful";
    public static final String TRANSFER_FAILED = "Invalid transfer: insufficient funds or invalid accounts";

    public static final String TRANSFER_APPROVED = "Transfer approved and processed immediately";
    public static final String TRANSFER_BLOCKED = "Transfer blocked due to fraud detection";
    public static final String TRANSFER_REQUIRES_MANUAL_REVIEW = "Transfer requires manual review";
    public static final String ADDITIONAL_VERIFICATION_REQUIRED = "Additional verification required";

    public static final String GET_ACCOUNT_TRANSACTIONS_FORBIDDEN = "You do not have permission to access this account";
}
