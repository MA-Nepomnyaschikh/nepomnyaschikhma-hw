package api.iteration_1;

import api.BaseTest;
import io.restassured.response.ValidatableResponse;
import models.request.CreateUserRequestDto;
import models.request.LoginUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import supports.StepLogger;
import supports.assertions.UserAssertions;

import static testdata.AuthData.*;
import static testdata.UserData.ADMIN_ROLE;
import static testdata.UserData.generateUserDto;

public class LoginUserTest extends BaseTest {

    @DisplayName("API. Администратор может авторизоваться")
    @Test
    public void adminCanGenerateAuthTokenTest() {
        CreateUserRequestDto userDto = generateUserDto(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_ROLE);

        LoginUserRequestDto loginDto = generateLoginDto(userDto);

        ValidatableResponse loginUserResponse = StepLogger.log("Авторизовать администратора", () -> {
            return authSteps.login(loginDto);
        });

        StepLogger.log("Проверить авторизацию администратора", () -> {
            UserAssertions.assertUserLoggedIn(softly, loginUserResponse, userDto);
        });
    }

    @DisplayName("API. Пользователь может авторизоваться")
    @Test
    public void userCanGenerateAuthTokenTest() {
        CreateUserRequestDto userDto = StepLogger.log("Создать пользователя", () -> {
            return userSteps.createRandomUser();
        });

        LoginUserRequestDto loginDto = generateLoginDto(userDto);

        ValidatableResponse loginUserResponse = StepLogger.log("Авторизовать пользователя", () -> {
            return authSteps.login(loginDto);
        });

        StepLogger.log("Проверить авторизацию пользователя", () -> {
            UserAssertions.assertUserLoggedIn(softly, loginUserResponse, userDto);
        });
    }
}
