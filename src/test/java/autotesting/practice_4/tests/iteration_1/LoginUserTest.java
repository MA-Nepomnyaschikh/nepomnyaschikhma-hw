package autotesting.practice_4.tests.iteration_1;

import autotesting.practice_4.tests.BaseTest;
import autotesting.practice_4.models.request.CreateUserRequestDto;
import autotesting.practice_4.models.request.LoginUserRequestDto;
import autotesting.practice_4.models.response.LoginUserResponseDto;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static autotesting.practice_4.specs.ResponseSpecs.AUTH_HEADER;
import static autotesting.practice_4.testdata.AuthData.*;
import static autotesting.practice_4.testdata.UserData.*;

public class LoginUserTest extends BaseTest {

    @Test
    public void adminCanGenerateAuthTokenTest() {
        CreateUserRequestDto userDto = generateUserDto(ADMIN_LOGIN, ADMIN_PASSWORD, ADMIN_ROLE);

        LoginUserRequestDto loginDto = generateLoginDto(userDto);
        ValidatableResponse loginUserResponse = authSteps.login(loginDto);

        String authHeader = loginUserResponse.extract().header(AUTH_HEADER);
        LoginUserResponseDto loginResponseDto = loginUserResponse.extract().as(LoginUserResponseDto.class);

        softly.assertThat(authHeader).isNotBlank();
        softly.assertThat(loginResponseDto.getRole()).isEqualTo(ADMIN_ROLE);
        softly.assertThat(loginResponseDto.getUsername()).isEqualTo(loginDto.getUsername());
    }

    @Test
    public void userCanGenerateAuthTokenTest() {
        CreateUserRequestDto userDto = adminSteps.createRandomUser();

        LoginUserRequestDto loginDto = generateLoginDto(userDto);
        ValidatableResponse loginUserResponse = authSteps.login(loginDto);

        String authHeader = loginUserResponse.extract().header(AUTH_HEADER);
        LoginUserResponseDto loginResponseDto = loginUserResponse.extract().as(LoginUserResponseDto.class);

        softly.assertThat(authHeader).isNotBlank();
        softly.assertThat(loginResponseDto.getRole()).isEqualTo(USER_ROLE);
        softly.assertThat(loginResponseDto.getUsername()).isEqualTo(loginDto.getUsername());
    }
}
