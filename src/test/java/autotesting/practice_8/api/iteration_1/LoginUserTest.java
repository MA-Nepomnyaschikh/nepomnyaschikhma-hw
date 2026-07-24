package autotesting.practice_8.api.iteration_1;

import autotesting.practice_8.BaseTest;
import autotesting.practice_8.models.request.CreateUserRequestDto;
import autotesting.practice_8.models.request.LoginUserRequestDto;
import autotesting.practice_8.models.response.LoginUserResponseDto;
import autotesting.practice_8.supports.assertions.UserAssertions;
import autotesting.practice_8.supports.comparisons.UserComparisons;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static autotesting.practice_8.specs.ResponseSpecs.AUTH_HEADER;
import static autotesting.practice_8.testdata.AuthData.*;
import static autotesting.practice_8.testdata.UserData.ADMIN_ROLE;
import static autotesting.practice_8.testdata.UserData.generateUserDto;

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
