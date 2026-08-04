package api.iteration_2;

import api.BaseTest;
import models.request.UpdateUserRequestDto;
import models.response.CreateUserResponseDto;
import models.response.UpdateUserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.context.TestUser;
import testdata.randommodelgenerator.RandomModelGenerator;

import java.util.stream.Stream;

import static testdata.UserData.generateUpdateUserDto;
import static testdata.expectedmessages.api.UserApiMessages.PROFILE_UPDATE_INVALID_NAME;
import static testdata.expectedmessages.api.UserApiMessages.PROFILE_UPDATE_SUCCESS;

public class UpdateCustomerProfileTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может изменить имя в профиле на валидное")
    @Test
    @UserSession
    public void authorizedUserCanSetValidName(TestUser user) {
        UpdateUserRequestDto updateUserDto = RandomModelGenerator.generate(UpdateUserRequestDto.class);

        UpdateUserResponseDto updatedUser = StepLogger.log("Изменить имя в профиле", () -> {
            return userSteps.updateCustomerProfile(user.getToken(), updateUserDto);
        });

        StepLogger.log("Проверить изменение имени", () -> {
            softly.assertThat(updatedUser.getMessage()).isEqualTo(PROFILE_UPDATE_SUCCESS);
            softly.assertThat(updatedUser.getCustomer().getName()).isEqualTo(updateUserDto.getName());
        });

        StepLogger.log("Проверить профиль после изменения имени", () -> {
            CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
            softly.assertThat(actualUser.getName()).isEqualTo(updateUserDto.getName());
        });
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

    @DisplayName("API. Авторизованный пользователь не может изменить имя в профиле на невалидное")
    @MethodSource("invalidNameProvider")
    @ParameterizedTest
    @UserSession
    public void authorizedUserCannotSetInvalidName(UpdateUserRequestDto updateUserDto, TestUser user) {
        String errorResponse = StepLogger.log("Изменить имя в профиле на невалидное", () -> {
            return userSteps.updateCustomerProfile(
                updateUserDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.badRequest())
                .extract().asString();
        });

        StepLogger.log("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(PROFILE_UPDATE_INVALID_NAME);
        });

        StepLogger.log("Проверить отсутствие изменений в профиле", () -> {
            CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
            softly.assertThat(actualUser.getName()).isNull();
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может изменить имя в профиле")
    @Test
    @UserSession
    public void unauthorizedUserCannotChangeName(TestUser user) {
        UpdateUserRequestDto updateUserDto = RandomModelGenerator.generate(UpdateUserRequestDto.class);

        StepLogger.log("Изменить имя в профиле без авторизации", () -> {
            userSteps.updateCustomerProfile(updateUserDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.log("Проверить отсутствие изменений в профиле", () -> {
            CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
            softly.assertThat(actualUser.getName()).isNull();
        });
    }
}
