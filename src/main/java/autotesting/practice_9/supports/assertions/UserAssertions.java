package autotesting.practice_9.supports.assertions;

import autotesting.practice_9.models.request.CreateUserRequestDto;
import autotesting.practice_9.models.response.CreateUserResponseDto;
import autotesting.practice_9.models.response.LoginUserResponseDto;
import autotesting.practice_9.supports.comparisons.UserComparisons;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;

import static autotesting.practice_9.specs.ResponseSpecs.AUTH_HEADER;

public final class UserAssertions {

    public static void assertUserCreated(SoftAssertions softly, CreateUserResponseDto response, CreateUserRequestDto request) {
        softly.assertThat(response)
                .usingRecursiveComparison()
                .comparingOnlyFields(UserComparisons.CREATE_USER.fields())
                .isEqualTo(request);

        softly.assertThat(response.getId()).isNotNull().isPositive();
        softly.assertThat(response.getPassword()).isNotNull().isNotEqualTo(request.getPassword());
        softly.assertThat(response.getAccounts()).isEmpty();
    }

    public static void assertUserLoggedIn(SoftAssertions softly, ValidatableResponse loginUserResponse, CreateUserRequestDto userDto) {
        String authHeader = loginUserResponse.extract().header(AUTH_HEADER);
        LoginUserResponseDto loginResponseDto = loginUserResponse.extract().as(LoginUserResponseDto.class);

        softly.assertThat(authHeader).isNotBlank();
        softly.assertThat(loginResponseDto)
                .usingRecursiveComparison()
                .comparingOnlyFields(UserComparisons.LOGIN_USER.fields())
                .isEqualTo(userDto);
    }
}
