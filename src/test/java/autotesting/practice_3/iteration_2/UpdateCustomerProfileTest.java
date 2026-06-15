package autotesting.practice_3.iteration_2;

import autotesting.practice_3.generators.TestData;
import autotesting.practice_3.contract.enams.UserRole;
import autotesting.practice_3.contract.models.request.CreateUserRequestDto;
import autotesting.practice_3.contract.models.request.LoginUserRequestDto;
import autotesting.practice_3.contract.models.request.UpdateAccountRequestDto;
import autotesting.practice_3.contract.models.response.CreateUserResponseDto;
import autotesting.practice_3.contract.models.response.UpdateAccountResponseDto;
import autotesting.practice_3.BaseTest;
import autotesting.practice_3.requests.get.GetCustomerProfileRequest;
import autotesting.practice_3.requests.post.CreateUserRequest;
import autotesting.practice_3.requests.post.LoginUserRequest;
import autotesting.practice_3.requests.post.UpdateAccountRequest;
import autotesting.practice_3.specs.RequestSpecs;
import autotesting.practice_3.specs.ResponseSpecs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static autotesting.practice_3.contract.messages.ProfileUpdateMessages.PROFILE_UPDATE_INVALID_NAME;
import static autotesting.practice_3.contract.messages.ProfileUpdateMessages.PROFILE_UPDATE_SUCCESS;
import static autotesting.practice_3.specs.ResponseSpecs.AUTH_HEADER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateCustomerProfileTest extends BaseTest {

    @Test
    public void authorizedUserCanSetValidName() {
        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(user);

        LoginUserRequestDto loginRequestDto = LoginUserRequestDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        String userAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginRequestDto)
                .extract().header(AUTH_HEADER);

        String newName = TestData.getName();

        UpdateAccountRequestDto updateAccountRequestDto = UpdateAccountRequestDto.builder()
                .name(newName)
                .build();

        UpdateAccountResponseDto response = new UpdateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .put(updateAccountRequestDto)
                .extract().as(UpdateAccountResponseDto.class);

        softly.assertThat(response.getMessage()).isEqualTo(PROFILE_UPDATE_SUCCESS);
        softly.assertThat(response.getCustomer().getName()).isEqualTo(newName);

        CreateUserResponseDto customer = new GetCustomerProfileRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract().as(CreateUserResponseDto.class);

        softly.assertThat(customer.getName()).isEqualTo(newName);
    }

    public static Stream<Arguments> invalidNameProvider() {
        return Stream.of(
                Arguments.of("Mikhail"),
                Arguments.of("Nepomnyaschikh Mikhail Aleksandrovich"),
                Arguments.of("Mikhail Nepomnyaschikh1"),
                Arguments.of("Mikhail! Nepomnyaschikh"),
                Arguments.of("Mikhail  Nepomnyaschikh"),
                Arguments.of("Mikhail Nepomnyaschikh "),
                Arguments.of(" Mikhail")
        );
    }

    @MethodSource("invalidNameProvider")
    @ParameterizedTest
    public void authorizedUserCannotSetInvalidName(String name) {
        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(user);

        LoginUserRequestDto loginRequestDto = LoginUserRequestDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        String userAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginRequestDto)
                .extract().header(AUTH_HEADER);

        UpdateAccountRequestDto updateAccountRequestDto = UpdateAccountRequestDto.builder()
                .name(name)
                .build();

        String errorResponse = new UpdateAccountRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.badRequest())
                .put(updateAccountRequestDto)
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(PROFILE_UPDATE_INVALID_NAME);

        CreateUserResponseDto customer = new GetCustomerProfileRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract().as(CreateUserResponseDto.class);

        softly.assertThat(customer.getName()).isNull();
    }

    @Test
    public void unauthorizedUserCannotChangeName() {
        CreateUserRequestDto user = CreateUserRequestDto.builder()
                .username(TestData.getUsername())
                .password(TestData.getPassword())
                .role(UserRole.USER.toString())
                .build();

        new CreateUserRequest(
                RequestSpecs.authAsAdmin(),
                ResponseSpecs.created())
                .post(user);

        LoginUserRequestDto loginRequestDto = LoginUserRequestDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        String userAuthHeader = new LoginUserRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.ok())
                .post(loginRequestDto)
                .extract().header(AUTH_HEADER);

        UpdateAccountRequestDto updateAccountRequestDto = UpdateAccountRequestDto.builder()
                .name(TestData.getName())
                .build();

        new UpdateAccountRequest(
                RequestSpecs.unauth(),
                ResponseSpecs.unauthorized())
                .put(updateAccountRequestDto);

        CreateUserResponseDto customer = new GetCustomerProfileRequest(
                RequestSpecs.authAsUser(userAuthHeader),
                ResponseSpecs.ok())
                .get()
                .extract().as(CreateUserResponseDto.class);

        softly.assertThat(customer.getName()).isNull();
    }
}
