package autotesting.practice_7.api.iteration_1;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.models.request.LoginUserRequestDto;
import autotesting.practice_7.models.response.LoginUserResponseDto;
import autotesting.practice_7.BaseTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static autotesting.practice_7.specs.ResponseSpecs.AUTH_HEADER;
import static autotesting.practice_7.testdata.AuthData.*;
import static autotesting.practice_7.testdata.UserData.*;

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
        CreateUserRequestDto userDto = userSteps.createRandomUser();

        LoginUserRequestDto loginDto = generateLoginDto(userDto);
        ValidatableResponse loginUserResponse = authSteps.login(loginDto);

        String authHeader = loginUserResponse.extract().header(AUTH_HEADER);
        LoginUserResponseDto loginResponseDto = loginUserResponse.extract().as(LoginUserResponseDto.class);

        softly.assertThat(authHeader).isNotBlank();
        softly.assertThat(loginResponseDto.getRole()).isEqualTo(USER_ROLE);
        softly.assertThat(loginResponseDto.getUsername()).isEqualTo(loginDto.getUsername());
    }
}
