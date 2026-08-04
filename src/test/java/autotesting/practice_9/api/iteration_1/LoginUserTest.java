package autotesting.practice_9.api.iteration_1;

import autotesting.practice_9.models.request.CreateUserRequestDto;
import autotesting.practice_9.models.request.LoginUserRequestDto;
import autotesting.practice_9.supports.assertions.UserAssertions;
import autotesting.practice_9.BaseTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static autotesting.practice_9.testdata.AuthData.*;
import static autotesting.practice_9.testdata.UserData.ADMIN_ROLE;
import static autotesting.practice_9.testdata.UserData.generateUserDto;

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
