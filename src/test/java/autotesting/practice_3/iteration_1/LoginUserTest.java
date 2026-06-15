package autotesting.practice_3.iteration_1;

import autotesting.practice_3.generators.TestData;
import autotesting.practice_3.contract.enams.UserRole;
import autotesting.practice_3.contract.models.request.CreateUserRequestDto;
import autotesting.practice_3.contract.models.request.LoginUserRequestDto;
import autotesting.practice_3.contract.models.response.LoginUserResponseDto;
import autotesting.practice_3.BaseTest;
import autotesting.practice_3.requests.post.CreateUserRequest;
import autotesting.practice_3.requests.post.LoginUserRequest;
import autotesting.practice_3.specs.RequestSpecs;
import autotesting.practice_3.specs.ResponseSpecs;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static autotesting.practice_3.generators.TestData.ADMIN_LOGIN;
import static autotesting.practice_3.generators.TestData.ADMIN_PASSWORD;
import static autotesting.practice_3.specs.ResponseSpecs.AUTH_HEADER;

public class LoginUserTest extends BaseTest {

    @Test
    public void adminCanGenerateAuthTokenTest() {
        LoginUserRequestDto loginUserRequest = LoginUserRequestDto.builder()
                .username(ADMIN_LOGIN)
                .password(ADMIN_PASSWORD)
                .build();

        ValidatableResponse loginUserResponse = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginUserRequest);

        softly.assertThat(loginUserResponse.extract().header(AUTH_HEADER)).isNotNull();

        LoginUserResponseDto dto = loginUserResponse.extract().as(LoginUserResponseDto.class);

        softly.assertThat(dto.getRole()).isEqualTo(UserRole.ADMIN.toString());
        softly.assertThat(dto.getUsername()).isEqualTo(ADMIN_LOGIN);
    }

    @Test
    public void userCanGenerateAuthTokenTest() {
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(createUserRequestDto);

        LoginUserRequestDto loginUserRequestDto = LoginUserRequestDto.builder()
                .username(createUserRequestDto.getUsername())
                .password(createUserRequestDto.getPassword())
                .build();

        ValidatableResponse loginUserResponse = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginUserRequestDto);

        softly.assertThat(loginUserResponse.extract().header(AUTH_HEADER)).isNotNull();

        LoginUserResponseDto dto = loginUserResponse.extract().as(LoginUserResponseDto.class);

        softly.assertThat(dto.getRole()).isEqualTo(UserRole.USER.toString());
        softly.assertThat(dto.getUsername()).isEqualTo(createUserRequestDto.getUsername());
    }
}
