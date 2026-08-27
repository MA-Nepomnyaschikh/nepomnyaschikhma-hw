package supports.assertions;

import models.api.request.CreateUserRequestDto;
import models.api.response.CreateUserResponseDto;
import models.api.response.GetUserResponseDto;
import models.api.response.LoginUserResponseDto;
import supports.comparisons.UserComparisonFields;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;

import static specs.ResponseSpecs.AUTH_HEADER;

public final class UserAssertions {

    public static void assertCreateUserCompleted(SoftAssertions softly, CreateUserResponseDto response, CreateUserRequestDto request) {
        softly.assertThat(request)
                .usingRecursiveComparison()
                .comparingOnlyFields(UserComparisonFields.CREATE_USER_REQUEST_TO_CREATE_USER_RESPONSE.fields())
                .isEqualTo(response);

        softly.assertThat(response.getId()).isNotNull().isPositive();
        softly.assertThat(response.getPassword()).isNotNull().isNotEqualTo(request.getPassword());
        softly.assertThat(response.getAccounts()).isEmpty();
    }

    public static void assertUserCreated(SoftAssertions softly, GetUserResponseDto response, CreateUserRequestDto request) {
        softly.assertThat(request)
                .usingRecursiveComparison()
                .comparingOnlyFields(UserComparisonFields.CREATE_USER_REQUEST_TO_GET_USER_RESPONSE.fields())
                .isEqualTo(response);

        softly.assertThat(response.getId()).isNotNull().isPositive();
        softly.assertThat(response.getAccounts()).isEmpty();
    }

    public static void assertLoginUserCompleted(SoftAssertions softly, ValidatableResponse loginUserResponse, CreateUserRequestDto userDto) {
        String authHeader = loginUserResponse.extract().header(AUTH_HEADER);
        LoginUserResponseDto loginResponseDto = loginUserResponse.extract().as(LoginUserResponseDto.class);

        softly.assertThat(authHeader).isNotBlank();
        softly.assertThat(loginResponseDto)
                .usingRecursiveComparison()
                .comparingOnlyFields(UserComparisonFields.LOGIN_USER_RESPONSE_TO_CREATE_USER_REQUEST.fields())
                .isEqualTo(userDto);
    }
}
