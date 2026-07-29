package autotesting.practice_7.api.iteration_1;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.models.response.CreateUserResponseDto;
import autotesting.practice_7.models.response.ErrorResponseDto;
import autotesting.practice_7.specs.RequestSpecs;
import autotesting.practice_7.specs.ResponseSpecs;
import autotesting.practice_7.BaseTest;
import autotesting.practice_7.supports.comparisons.UserComparisons;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static autotesting.practice_7.validation_messages.api.UserApiMessages.CREATE_USER_DUPLICATE_USERNAME;
import static autotesting.practice_7.validation_messages.api.UserApiMessages.USER_CREATE_FORBIDDEN;
import static autotesting.practice_7.testdata.UserData.*;

public class CreateUserTest extends BaseTest {

    public static Stream<Arguments> validUserDataProvider() {
        return Stream.of(
                Arguments.of(generateUserDto(getUsername(), getPassword(),USER_ROLE)),
                Arguments.of(generateUserDto(getUsername(), getPassword(),ADMIN_ROLE))
                );
    }

    @MethodSource("validUserDataProvider")
    @ParameterizedTest
    public void adminCanCreateUserWithValidDataTest(CreateUserRequestDto userDto) {
        CreateUserResponseDto createdUser = userSteps.createUser(userDto);

        softly.assertThat(createdUser)
                .usingRecursiveComparison()
                .comparingOnlyFields(UserComparisons.CREATE_USER.fields())
                .isEqualTo(userDto);

        softly.assertThat(createdUser.getId()).isNotNull().isPositive();
        softly.assertThat(createdUser.getPassword()).isNotNull().isNotEqualTo(userDto.getPassword());
        softly.assertThat(createdUser.getAccounts()).isEmpty();

        CreateUserResponseDto actualUser = userSteps.getUserById(createdUser.getId());

        softly.assertThat(actualUser)
                .usingRecursiveComparison()
                .isEqualTo(createdUser);
    }

    public static Stream<Arguments> invalidUserDataProvider() {
        return Stream.of(
                Arguments.of(generateUserDto("  ", "test$X2p", USER_ROLE), "username", "Username cannot be blank"),
                Arguments.of(generateUserDto("ab", "test$X2p", USER_ROLE), "username", "Username must be between 3 and 15 characters"),
                Arguments.of(generateUserDto("qwertyuiopasdfgh", "test$X2p", USER_ROLE), "username", "Username must be between 3 and 15 characters"),
                Arguments.of(generateUserDto("abc1$", "test$X2p", USER_ROLE), "username", "Username must contain only letters, digits, dashes, underscores, and dots"),
                Arguments.of(generateUserDto("testX2p", "", USER_ROLE), "password", "Password cannot be blank"),
                Arguments.of(generateUserDto("testX2p", "pass$1R", USER_ROLE), "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of(generateUserDto("testX2p", "passworD$", USER_ROLE), "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of(generateUserDto("testX2p", "PASSWORD$1", USER_ROLE), "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of(generateUserDto("testX2p", "password$1", USER_ROLE), "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of(generateUserDto("testX2p", "passworD1", USER_ROLE), "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of(generateUserDto("testX2p", "pass worD1$", USER_ROLE), "password", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"),
                Arguments.of(generateUserDto("testX2p", "test$X2p", "TEST"), "role", "Role must be either 'ADMIN' or 'USER'")
        );
    }

    @MethodSource("invalidUserDataProvider")
    @ParameterizedTest
    public void adminCannotCreateUserWithInvalidDataTest(CreateUserRequestDto userDto, String errorKey, String errorValue) {
        Map<String, List<String>> errors = userSteps.createUser(
                userDto, RequestSpecs.authAsAdmin(), ResponseSpecs.badRequest())
                .extract().as(new TypeRef<Map<String, List<String>>>() {});

        softly.assertThat(errors).containsKey(errorKey);
        softly.assertThat(errors.get(errorKey)).contains(errorValue);

        List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();

        softly.assertThat(allUsers)
                .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                .isEmpty();
    }

    @Test
    public void adminCannotCreateUserWithExistingUsernameTest() {
        CreateUserRequestDto userDto = generateRandomUserDto();
        userSteps.createUser(userDto);

        String errorResponse = userSteps.createUser(
                userDto, RequestSpecs.authAsAdmin(), ResponseSpecs.badRequest())
                .extract().asString();

        softly.assertThat(errorResponse)
                .isEqualTo(CREATE_USER_DUPLICATE_USERNAME.formatted(userDto.getUsername()));

        List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();

        softly.assertThat(allUsers)
                .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                .singleElement();
    }

    @Test
    public void unauthorizedUserCannotCreateUserTest() {
        CreateUserRequestDto userDto = generateRandomUserDto();
        userSteps.createUser(
                userDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());

        List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();

        softly.assertThat(allUsers)
                .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                .isEmpty();
    }

    @Test
    public void userWithoutAdminPermissionsCannotCreateUserTest() {
        CreateUserRequestDto userWithoutAdminPermissionsDto = generateRandomUserDto();
        userSteps.createUser(userWithoutAdminPermissionsDto);
        String userWithoutAdminPermissionsAuthHeader = authSteps.loginAndGetToken(userWithoutAdminPermissionsDto);

        CreateUserRequestDto userDto = generateRandomUserDto();

        ErrorResponseDto errorResponse = userSteps.createUser(
                userDto, RequestSpecs.authAsUser(userWithoutAdminPermissionsAuthHeader), ResponseSpecs.forbidden())
                .extract().as(ErrorResponseDto.class);

        softly.assertThat(errorResponse.getError()).isEqualTo(USER_CREATE_FORBIDDEN);

        List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();

        softly.assertThat(allUsers)
                .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                .isEmpty();
    }
}
