package api.iteration_3;

import api.BaseTest;
import models.response.CreateUserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.context.TestUser;

public class GetCustomerProfileTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может получить свой профиль")
    @Test
    @UserSession
    public void authorizedUserCanGetProfileTest(TestUser user) {
        CreateUserResponseDto actualUser = StepLogger.log("Получить профиль пользователя", () -> {
            return userSteps.getCustomerProfile(user.getToken());
        });

        StepLogger.log("Проверить профиль пользователя", () -> {
            softly.assertThat(actualUser)
                    .usingRecursiveComparison()
                    .isEqualTo(user.getResponseDto());
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может получить свой профиль")
    @Test
    public void unauthorizedUserCannotGetProfileTest() {
        StepLogger.log("Получить профиль пользователя без авторизации", () -> {
            return userSteps.getCustomerProfile(RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });
    }
}
