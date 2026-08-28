package common.testdata.messages.api;

public class UserApiMessages {
    public static final String CREATE_USER_DUPLICATE_USERNAME = "Error: Username '%s' already exists.";
    public static final String CREATE_USER_FORBIDDEN = "Forbidden";

    public static final String LOGIN_USER_INVALID_DATA = "Invalid username or password";

    public static final String PROFILE_UPDATE_SUCCESSFULLY = "Profile updated successfully";
    public static final String PROFILE_UPDATE_INVALID_NAME = "Name must contain two words with letters only";

    public static final String DELETE_USER_SUCCESSFULLY = "User with ID %s deleted successfully." ;
    public static final String DELETE_USER_FORBIDDEN = "Forbidden" ;
    public static final String DELETE_USER_INVALID_ID = "Error: User with ID %s not found." ;

    public static final String GET_USERS_LIST_FORBIDDEN = "Forbidden";

}
