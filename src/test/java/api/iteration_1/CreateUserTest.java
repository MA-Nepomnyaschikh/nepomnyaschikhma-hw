package api.iteration_1;

import api.BaseTest;
import models.request.CreateUserRequestDto;
import models.response.CreateUserResponseDto;
import models.response.ErrorResponseDto;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.assertions.UserAssertions;
import testdata.randommodelgenerator.RandomModelGenerator;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static testdata.UserData.*;
import static testdata.expectedmessages.api.UserApiMessages.CREATE_USER_DUPLICATE_USERNAME;
import static testdata.expectedmessages.api.UserApiMessages.USER_CREATE_FORBIDDEN;

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

        UserAssertions.assertUserCreated(softly, createdUser, userDto);

        CreateUserResponseDto actualUser = userSteps.getUserById(createdUser.getId());
        softly.assertThat(actualUser)
                .usingRecursiveComparison()
                .isEqualTo(createdUser);
    }

    public static Stream<Arguments> invalidUserDataProvider() {
        return Stream.of(
                Arguments.of(generateUserDto("   ", "test$X2p", USER_ROLE), "username", List.of("Username cannot be blank", "Username must contain only letters, digits, dashes, underscores, and dots")),
                Arguments.of(generateUserDto("ab", "test$X2p", USER_ROLE), "username", List.of("Username must be between 3 and 15 characters")),
                Arguments.of(generateUserDto("qwertyuiopasdfgh", "test$X2p", USER_ROLE), "username", List.of("Username must be between 3 and 15 characters")),
                Arguments.of(generateUserDto("abc1$", "test$X2p", USER_ROLE), "username", List.of("Username must contain only letters, digits, dashes, underscores, and dots")),
                Arguments.of(generateUserDto("testX2p", "", USER_ROLE), "password", List.of("Password cannot be blank", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of(generateUserDto("testX2p", "pass$1R", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of(generateUserDto("testX2p", "passworD$", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of(generateUserDto("testX2p", "PASSWORD$1", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of(generateUserDto("testX2p", "password$1", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of(generateUserDto("testX2p", "passworD1", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of(generateUserDto("testX2p", "pass worD1$", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of(generateUserDto("testX2p", "test$X2p", "TEST"), "role", List.of("Role must be either 'ADMIN' or 'USER'"))
        );
    }

    @MethodSource("invalidUserDataProvider")
    @ParameterizedTest
    public void adminCannotCreateUserWithInvalidDataTest(CreateUserRequestDto userDto, String errorKey, List<String> errorValues) {
        Map<String, List<String>> errors = userSteps.createUser(
                userDto, RequestSpecs.authAsAdmin(), ResponseSpecs.badRequest())
                .extract().as(new TypeRef<Map<String, List<String>>>() {});

        softly.assertThat(errors).containsKey(errorKey);
        softly.assertThat(errors.get(errorKey)).containsExactlyInAnyOrderElementsOf(errorValues);

        List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();

        softly.assertThat(allUsers)
                .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                .isEmpty();
    }

    @Test
    public void adminCannotCreateUserWithExistingUsernameTest() {
        CreateUserRequestDto userDto = RandomModelGenerator.generate(CreateUserRequestDto.class);
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
        CreateUserRequestDto userDto = RandomModelGenerator.generate(CreateUserRequestDto.class);
        userSteps.createUser(
                userDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());

        List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();

        softly.assertThat(allUsers)
                .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                .isEmpty();
    }

    @Test
    public void userWithoutAdminPermissionsCannotCreateUserTest() {
        CreateUserRequestDto userWithoutAdminPermissionsDto = RandomModelGenerator.generate(CreateUserRequestDto.class);
        userSteps.createUser(userWithoutAdminPermissionsDto);
        String userWithoutAdminPermissionsAuthHeader = authSteps.loginAndGetToken(userWithoutAdminPermissionsDto);

        CreateUserRequestDto userDto = RandomModelGenerator.generate(CreateUserRequestDto.class);

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
