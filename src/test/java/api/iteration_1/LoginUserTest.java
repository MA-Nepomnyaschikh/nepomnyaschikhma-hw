package api.iteration_1;

import api.BaseTest;
import models.request.CreateUserRequestDto;
import models.request.LoginUserRequestDto;
import supports.assertions.UserAssertions;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static testdata.AuthData.*;
import static testdata.UserData.ADMIN_ROLE;
import static testdata.UserData.generateUserDto;

public class LoginUserTest extends BaseTest {

    @Test
    public void adminCanGenerateAuthTokenTest() {
        CreateUserRequestDto userDto = generateUserDto(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_ROLE);

        LoginUserRequestDto loginDto = generateLoginDto(userDto);
        ValidatableResponse loginUserResponse = authSteps.login(loginDto);

        UserAssertions.assertUserLoggedIn(softly, loginUserResponse, userDto);
    }

    @Test
    public void userCanGenerateAuthTokenTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();

        LoginUserRequestDto loginDto = generateLoginDto(userDto);
        ValidatableResponse loginUserResponse = authSteps.login(loginDto);

        UserAssertions.assertUserLoggedIn(softly, loginUserResponse, userDto);
    }
}
