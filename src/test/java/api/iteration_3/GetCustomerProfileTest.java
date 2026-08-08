package api.iteration_3;

import api.BaseTest;
import models.response.CreateUserResponseDto;
import models.response.ErrorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.context.TestUser;

import static testdata.expectedmessages.api.UserApiMessages.CREATE_USER_FORBIDDEN;

@DisplayName("API. Получение профиля пользователя")
public class GetCustomerProfileTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может получить свой профиль")
    @Test
    @UserSession
    public void authorizedUserCanGetProfileTest(TestUser user) {
        CreateUserResponseDto actualUser = StepLogger.apiStep("Получить профиль пользователя", () -> {
            return userSteps.getCustomerProfile(user.getToken());
        });

        StepLogger.apiStep("Проверить профиль пользователя", () -> {
            softly.assertThat(actualUser)
                    .usingRecursiveComparison()
                    .isEqualTo(user.getResponseDto());
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может получить свой профиль")
    @Test
    public void unauthorizedUserCannotGetProfileTest() {
        StepLogger.apiStep("Получить профиль пользователя без авторизации", () -> {
            return userSteps.getCustomerProfile(RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });
    }

    @DisplayName("API. Администратор не может получить профиль")
    @Test
    public void adminCannotGetProfileTest() {
        ErrorResponseDto errorResponse = StepLogger.apiStep("Получить профиль администратором", () -> {
            return userSteps.getCustomerProfile(
                    RequestSpecs.authAsAdmin(), ResponseSpecs.forbidden())
                    .extract().as(ErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить ошибку при получении профиля", () -> {
            softly.assertThat(errorResponse.getError()).isEqualTo(CREATE_USER_FORBIDDEN);
        });
    }
}
