package api.iteration_1;

import api.BaseTest;
import io.restassured.response.ValidatableResponse;
import models.api.request.CreateUserRequestDto;
import models.api.request.LoginUserRequestDto;
import models.api.response.ErrorResponseDto;
import models.api.response.ValidationErrorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.assertions.UserAssertions;

import java.util.stream.Stream;

import static testdata.AuthData.*;
import static testdata.UserData.ADMIN_ROLE;
import static testdata.UserData.generateUserDto;
import static testdata.expectedmessages.api.UserApiMessages.LOGIN_USER_INVALID_DATA;

@DisplayName("API. Авторизация пользователя")
public class LoginUserTest extends BaseTest {

    @DisplayName("API. Администратор может авторизоваться")
    @Test
    public void adminCanGenerateAuthTokenTest() {
        CreateUserRequestDto userDto = generateUserDto(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_ROLE);

        LoginUserRequestDto loginDto = generateLoginDto(userDto);

        ValidatableResponse loginUserResponse = StepLogger.apiStep("Авторизовать администратора", () -> {
            return authSteps.login(loginDto);
        });

        StepLogger.apiStep("Проверить авторизацию администратора", () -> {
            UserAssertions.assertLoginUserCompleted(softly, loginUserResponse, userDto);
        });
    }

    @DisplayName("API. Пользователь может авторизоваться")
    @Test
    public void userCanGenerateAuthTokenTest() {
        CreateUserRequestDto userDto = StepLogger.apiStep("Создать пользователя", () -> {
            return userSteps.createRandomUser();
        });

        LoginUserRequestDto loginDto = generateLoginDto(userDto);

        ValidatableResponse loginUserResponse = StepLogger.apiStep("Авторизовать пользователя", () -> {
            return authSteps.login(loginDto);
        });

        StepLogger.apiStep("Проверить авторизацию пользователя", () -> {
            UserAssertions.assertLoginUserCompleted(softly, loginUserResponse, userDto);
        });
    }

    private static Stream<Arguments> invalidLoginDataProvider() {
        return Stream.of(
                Arguments.of("Пустой username", "", "ValidPass1!"),
                Arguments.of("Username короче 3 символов", "ab", "ValidPass1!"),
                Arguments.of("Username длиннее 15 символов", "abcdefghijklmnop", "ValidPass1!"),
                Arguments.of("Username содержит пробел", "user name", "ValidPass1!"),
                Arguments.of("Username содержит спецсимвол", "user@name", "ValidPass1!"),

                Arguments.of("Пустой password", "validUser", ""),
                Arguments.of("Password короче 8 символов", "validUser", "Aa1!abc"),
                Arguments.of("Password без цифры", "validUser", "Password!"),
                Arguments.of("Password без строчной буквы", "validUser", "PASSWORD1!"),
                Arguments.of("Password без заглавной буквы", "validUser", "password1!"),
                Arguments.of("Password без спецсимвола", "validUser", "Password1"),
                Arguments.of("Password содержит пробел", "validUser", "Pass word1!")
        );
    }

    @MethodSource("invalidLoginDataProvider")
    @DisplayName("API. Пользователь не может авторизоваться с невалидными данными")
    @ParameterizedTest(name = "{0}")
    public void userCannotGenerateAuthTokenWithInvalidDataTest(String testName, String username, String password) {
        LoginUserRequestDto loginDto = generateLoginDto(username, password);

        ValidationErrorResponseDto errorResponse = StepLogger.apiStep("Авторизовать пользователя", () -> {
            return authSteps.login(loginDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized())
                    .extract().as(ValidationErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить ошибку авторизации пользователя", () -> {
            softly.assertThat(errorResponse.getMessage()).isEqualTo(LOGIN_USER_INVALID_DATA);
        });
    }
}
