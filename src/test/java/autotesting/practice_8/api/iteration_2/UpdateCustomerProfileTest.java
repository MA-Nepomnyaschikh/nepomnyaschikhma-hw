package autotesting.practice_8.api.iteration_2;

import autotesting.practice_8.models.request.CreateUserRequestDto;
import autotesting.practice_8.models.request.UpdateUserRequestDto;
import autotesting.practice_8.models.response.CreateUserResponseDto;
import autotesting.practice_8.models.response.UpdateUserResponseDto;
import autotesting.practice_8.specs.RequestSpecs;
import autotesting.practice_8.specs.ResponseSpecs;
import autotesting.practice_8.testdata.UserData;
import autotesting.practice_8.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static autotesting.practice_8.testdata.UserData.generateUpdateUserDto;
import static autotesting.practice_8.expectedmessages.api.UserApiMessages.PROFILE_UPDATE_INVALID_NAME;
import static autotesting.practice_8.expectedmessages.api.UserApiMessages.PROFILE_UPDATE_SUCCESS;

public class UpdateCustomerProfileTest extends BaseTest {

    @Test
    public void authorizedUserCanSetValidName() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        UpdateUserRequestDto updateUserDto = UserData.generateRandomUpdateUserDto();
        UpdateUserResponseDto updatedUser = userSteps.updateCustomerProfile(userAuthHeader, updateUserDto);

        softly.assertThat(updatedUser.getMessage()).isEqualTo(PROFILE_UPDATE_SUCCESS);
        softly.assertThat(updatedUser.getCustomer().getName()).isEqualTo(updateUserDto.getName());

        CreateUserResponseDto actualUser = userSteps.getCustomerProfile(userAuthHeader);

        softly.assertThat(actualUser.getName()).isEqualTo(updateUserDto.getName());
    }

    public static Stream<Arguments> invalidNameProvider() {
        return Stream.of(
                Arguments.of(generateUpdateUserDto("Mikhail")),
                Arguments.of(generateUpdateUserDto("Nepomnyaschikh Mikhail Aleksandrovich")),
                Arguments.of(generateUpdateUserDto("Mikhail Nepomnyaschikh1")),
                Arguments.of(generateUpdateUserDto("Mikhail! Nepomnyaschikh")),
                Arguments.of(generateUpdateUserDto("Mikhail  Nepomnyaschikh")),
                Arguments.of(generateUpdateUserDto("Mikhail Nepomnyaschikh ")),
                Arguments.of(generateUpdateUserDto(" Mikhail"))
        );
    }

    @MethodSource("invalidNameProvider")
    @ParameterizedTest
    public void authorizedUserCannotSetInvalidName(UpdateUserRequestDto updateUserDto) {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        String errorResponse = userSteps.updateCustomerProfile(
                updateUserDto, RequestSpecs.authAsUser(userAuthHeader), ResponseSpecs.badRequest())
                .extract().asString();

        softly.assertThat(errorResponse).isEqualTo(PROFILE_UPDATE_INVALID_NAME);

        CreateUserResponseDto actualUser = userSteps.getCustomerProfile(userAuthHeader);

        softly.assertThat(actualUser.getName()).isNull();
    }

    @Test
    public void unauthorizedUserCannotChangeName() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String userAuthHeader = authSteps.loginAndGetToken(userDto);

        UpdateUserRequestDto updateUserDto = UserData.generateRandomUpdateUserDto();
        userSteps.updateCustomerProfile(updateUserDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());

        CreateUserResponseDto actualUser = userSteps.getCustomerProfile(userAuthHeader);

        softly.assertThat(actualUser.getName()).isNull();
    }
}
