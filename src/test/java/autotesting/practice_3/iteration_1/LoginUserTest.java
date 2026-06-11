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

public class LoginUserTest extends BaseTest {

    @Test
    public void adminCanGenerateAuthTokenTest() {
        LoginUserRequestDto loginUserRequest = LoginUserRequestDto.builder()
                .username("admin")
                .password("admin")
                .build();

        ValidatableResponse loginUserResponse = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginUserRequest);

        softly.assertThat(loginUserResponse.extract().header("Authorization")).isNotNull();

        LoginUserResponseDto dto = loginUserResponse.extract().as(LoginUserResponseDto.class);

        softly.assertThat(dto.getRole()).isEqualTo(UserRole.ADMIN);
        softly.assertThat(dto.getUsername()).isEqualTo("admin");
    }

    @Test
    public void userCanGenerateAuthTokenTest() {
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER)
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

        softly.assertThat(loginUserResponse.extract().header("Authorization")).isNotNull();

        LoginUserResponseDto dto = loginUserResponse.extract().as(LoginUserResponseDto.class);

        softly.assertThat(dto.getRole()).isEqualTo(UserRole.USER);
        softly.assertThat(dto.getUsername()).isEqualTo(createUserRequestDto.getUsername());
    }
}
