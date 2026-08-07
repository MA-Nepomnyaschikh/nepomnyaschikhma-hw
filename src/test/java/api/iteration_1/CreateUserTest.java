package api.iteration_1;

import api.BaseTest;
import io.restassured.common.mapper.TypeRef;
import models.request.CreateUserRequestDto;
import models.response.CreateUserResponseDto;
import models.response.ErrorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.assertions.UserAssertions;
import testdata.randommodelgenerator.RandomModelGenerator;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static testdata.UserData.*;
import static testdata.expectedmessages.api.UserApiMessages.*;

public class CreateUserTest extends BaseTest {

    public static Stream<Arguments> validUserDataProvider() {
        return Stream.of(
                Arguments.of("Создание пользователя без прав администратора", generateUserDto(getUsername(), getPassword(), USER_ROLE)),
                Arguments.of("Создание пользователя с правами администратора", generateUserDto(getUsername(), getPassword(), ADMIN_ROLE))
        );
    }

    @DisplayName("API. Администратор может создать пользователя с валидными данными")
    @MethodSource("validUserDataProvider")
    @ParameterizedTest(name= "{0}")
    public void adminCanCreateUserWithValidDataTest(String testName, CreateUserRequestDto userDto) {
        CreateUserResponseDto createdUser = StepLogger.log("Создать пользователя", () -> {
            return userSteps.createUser(userDto);
        });

        StepLogger.log("Проверить создание пользователя", () -> {
            UserAssertions.assertUserCreated(softly, createdUser, userDto);
        });

        StepLogger.log("Проверить наличие пользователя в системе", () -> {
            CreateUserResponseDto actualUser = userSteps.getUserById(createdUser.getId());
            softly.assertThat(actualUser)
                    .usingRecursiveComparison()
                    .isEqualTo(createdUser);
        });
    }

    public static Stream<Arguments> invalidUserDataProvider() {
        return Stream.of(
                Arguments.of("Пустой username", generateUserDto("   ", "test$X2p", USER_ROLE), "username", List.of("Username cannot be blank", "Username must contain only letters, digits, dashes, underscores, and dots")),
                Arguments.of("Username короче 3 символов", generateUserDto("ab", "test$X2p", USER_ROLE), "username", List.of("Username must be between 3 and 15 characters")),
                Arguments.of("Username длиннее 15 символов", generateUserDto("qwertyuiopasdfgh", "test$X2p", USER_ROLE), "username", List.of("Username must be between 3 and 15 characters")),
                Arguments.of("Username содержит пробел", generateUserDto("user name", "test$X2p", USER_ROLE), "username", List.of("Username must contain only letters, digits, dashes, underscores, and dots")),
                Arguments.of("Username содержит спецсимвол", generateUserDto("abc1$", "test$X2p", USER_ROLE), "username", List.of("Username must contain only letters, digits, dashes, underscores, and dots")),

                Arguments.of("Пустой password", generateUserDto("testX2p", "", USER_ROLE), "password", List.of("Password cannot be blank", "Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of("Password короче 8 символов", generateUserDto("testX2p", "pass$1R", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of("Password без цифры", generateUserDto("testX2p", "passworD$", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of("Password без строчной буквы", generateUserDto("testX2p", "PASSWORD$1", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of("Password без заглавной буквы", generateUserDto("testX2p", "password$1", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of("Password без спецсимвола", generateUserDto("testX2p", "passworD1", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),
                Arguments.of("Password содержит пробел", generateUserDto("testX2p", "pass worD1$", USER_ROLE), "password", List.of("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long")),

                Arguments.of("Невалидный role", generateUserDto("testX2p", "test$X2p", "TEST"), "role", List.of("Role must be either 'ADMIN' or 'USER'"))
        );
    }

    @DisplayName("API. Администратор не может создать пользователя с невалидными данными")
    @MethodSource("invalidUserDataProvider")
    @ParameterizedTest(name= "{0}")
    public void adminCannotCreateUserWithInvalidDataTest(String testName, CreateUserRequestDto userDto, String errorKey, List<String> errorValues) {
        Map<String, List<String>> errors = StepLogger.log("Создать пользователя с невалидными данными", () -> {
            return userSteps.createUser(
                    userDto, RequestSpecs.authAsAdmin(), ResponseSpecs.badRequest())
                    .extract().as(new TypeRef<Map<String, List<String>>>() {
                    });
        });

        StepLogger.log("Проверить ошибку при создании пользователя", () -> {
            softly.assertThat(errors).containsKey(errorKey);
            softly.assertThat(errors.get(errorKey)).containsExactlyInAnyOrderElementsOf(errorValues);
        });

        StepLogger.log("Проверить отсутствие пользователя в системе", () -> {
            List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();
            softly.assertThat(allUsers)
                    .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                    .isEmpty();
        });
    }

    @DisplayName("API. Администратор не может создать пользователя с уже существующим username")
    @Test
    public void adminCannotCreateUserWithExistingUsernameTest() {
        CreateUserRequestDto userDto = RandomModelGenerator.generate(CreateUserRequestDto.class);

        StepLogger.log("Создать пользователя", () -> {
            userSteps.createUser(userDto);
        });

        String errorResponse = StepLogger.log("Создать пользователя с таким же username", () -> {
            return userSteps.createUser(
                    userDto, RequestSpecs.authAsAdmin(), ResponseSpecs.badRequest())
                    .extract().asString();
        });

        StepLogger.log("Проверить ошибку при создании пользователя", () -> {
            softly.assertThat(errorResponse)
                    .isEqualTo(CREATE_USER_DUPLICATE_USERNAME.formatted(userDto.getUsername()));
        });

        StepLogger.log("Проверить отсутствие пользователя в системе", () -> {
            List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();
            softly.assertThat(allUsers)
                    .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                    .singleElement();
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может создать пользователя")
    @Test
    public void unauthorizedUserCannotCreateUserTest() {
        CreateUserRequestDto userDto = RandomModelGenerator.generate(CreateUserRequestDto.class);
        StepLogger.log("Создать пользователя без токена авторизации", () -> {
            userSteps.createUser(
                    userDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.log("Проверить отсутствие пользователя в системе", () -> {
            List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();
            softly.assertThat(allUsers)
                    .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                    .isEmpty();
        });
    }

    @DisplayName("API. Пользователь без прав администратора не может создать пользователя")
    @Test
    public void userWithoutAdminPermissionsCannotCreateUserTest() {
        CreateUserRequestDto userWithoutAdminPermissionsDto = RandomModelGenerator.generate(CreateUserRequestDto.class);

        String userWithoutAdminPermissionsAuthHeader = StepLogger.log("Создать пользователя", () -> {
            userSteps.createUser(userWithoutAdminPermissionsDto);
            return authSteps.loginAndGetToken(userWithoutAdminPermissionsDto);
        });

        CreateUserRequestDto userDto = RandomModelGenerator.generate(CreateUserRequestDto.class);

        ErrorResponseDto errorResponse = StepLogger.log("Создать пользователя с токеном пользователя", () -> {
            return userSteps.createUser(
                        userDto, RequestSpecs.authAsUser(userWithoutAdminPermissionsAuthHeader), ResponseSpecs.forbidden())
                        .extract().as(ErrorResponseDto.class);
        });

        StepLogger.log("Проверить ошибку при создании пользователя", () -> {
            softly.assertThat(errorResponse.getError()).isEqualTo(DELETE_USER_FORBIDDEN);
        });

        StepLogger.log("Проверить отсутствие пользователя в системе", () -> {
            List<CreateUserResponseDto> allUsers = userSteps.getAllUsers();
            softly.assertThat(allUsers)
                    .filteredOn(actualUser -> actualUser.getUsername().equals(userDto.getUsername()))
                    .isEmpty();
        });
    }
}
