package api.iteration_2;

import api.BaseTest;
import models.request.UpdateUserRequestDto;
import models.response.CreateUserResponseDto;
import models.response.ErrorResponseDto;
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
import static testdata.expectedmessages.api.UserApiMessages.*;

@DisplayName("API. Обновление профиля пользователя")
public class UpdateCustomerProfileTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может изменить имя в профиле на валидное")
    @Test
    @UserSession
    public void authorizedUserCanSetValidNameTest(TestUser user) {
        UpdateUserRequestDto updateUserDto = RandomModelGenerator.generate(UpdateUserRequestDto.class);

        UpdateUserResponseDto updatedUser = StepLogger.apiStep("Изменить имя в профиле", () -> {
            return userSteps.updateCustomerProfile(user.getToken(), updateUserDto);
        });

        StepLogger.apiStep("Проверить изменение имени", () -> {
            softly.assertThat(updatedUser.getMessage()).isEqualTo(PROFILE_UPDATE_SUCCESSFULLY);
            softly.assertThat(updatedUser.getCustomer().getName()).isEqualTo(updateUserDto.getName());
        });

        StepLogger.apiStep("Проверить профиль после изменения имени", () -> {
            CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
            softly.assertThat(actualUser.getName()).isEqualTo(updateUserDto.getName());
        });
    }

    public static Stream<Arguments> invalidNameProvider() {
        return Stream.of(
                Arguments.of("Имя из одного слова", generateUpdateUserDto("Mikhail")),
                Arguments.of("Имя из трех слов", generateUpdateUserDto("Nepomnyaschikh Mikhail Aleksandrovich")),
                Arguments.of("Имя с цифрой", generateUpdateUserDto("Mikhail Nepomnyaschikh1")),
                Arguments.of("Имя со спецсимволом", generateUpdateUserDto("Mikhail! Nepomnyaschikh")),
                Arguments.of("Имя с двумя пробелами", generateUpdateUserDto("Mikhail  Nepomnyaschikh")),
                Arguments.of("Имя с пробелом на конце", generateUpdateUserDto("Mikhail Nepomnyaschikh ")),
                Arguments.of("Имя с пробелом в начале", generateUpdateUserDto(" Mikhail"))
        );
    }

    @DisplayName("API. Авторизованный пользователь не может изменить имя в профиле на невалидное")
    @MethodSource("invalidNameProvider")
    @ParameterizedTest(name = "{0}")
    @UserSession
    public void authorizedUserCannotSetInvalidNameTest(String testName, UpdateUserRequestDto updateUserDto, TestUser user) {
        String errorResponse = StepLogger.apiStep("Изменить имя в профиле на невалидное", () -> {
            return userSteps.updateCustomerProfile(
                updateUserDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.badRequest())
                .extract().asString();
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(PROFILE_UPDATE_INVALID_NAME);
        });

        StepLogger.apiStep("Проверить отсутствие изменений в профиле", () -> {
            CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
            softly.assertThat(actualUser.getName()).isNull();
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может изменить имя в профиле")
    @Test
    @UserSession
    public void unauthorizedUserCannotChangeNameTest(TestUser user) {
        UpdateUserRequestDto updateUserDto = RandomModelGenerator.generate(UpdateUserRequestDto.class);

        StepLogger.apiStep("Изменить имя в профиле без авторизации", () -> {
            userSteps.updateCustomerProfile(updateUserDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.apiStep("Проверить отсутствие изменений в профиле", () -> {
            CreateUserResponseDto actualUser = userSteps.getCustomerProfile(user.getToken());
            softly.assertThat(actualUser.getName()).isNull();
        });
    }

    @DisplayName("API. Администратор не может изменить профиль")
    @Test
    public void adminCannotGetProfileTest() {
        UpdateUserRequestDto updateUserDto = RandomModelGenerator.generate(UpdateUserRequestDto.class);

        ErrorResponseDto errorResponse = StepLogger.apiStep("Изменить профиль администратором", () -> {
            return userSteps.updateCustomerProfile(
                    updateUserDto, RequestSpecs.authAsAdmin(), ResponseSpecs.forbidden())
                    .extract().as(ErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить ошибку при изменении профиля", () -> {
            softly.assertThat(errorResponse.getError()).isEqualTo(CREATE_USER_FORBIDDEN);
        });
    }
}
