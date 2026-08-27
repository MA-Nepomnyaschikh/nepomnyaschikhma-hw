package api.iteration_3;

import api.BaseTest;
import models.api.response.CreateUserResponseDto;
import models.api.response.ErrorResponseDto;
import models.db.Account;
import models.db.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.comparisons.UserComparisonFields;
import supports.context.TestUser;

import java.util.List;

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

        StepLogger.apiStep("Проверить профиль пользователя через API", () -> {
            softly.assertThat(actualUser)
                    .usingRecursiveComparison()
                    .isEqualTo(user.getResponseDto());
        });

        StepLogger.apiStep("Проверить профиль пользователя через БД", () -> {
            Customer customer = databaseSteps.getCustomerById(user.getId());
            softly.assertThat(customer)
                    .usingRecursiveComparison()
                    .comparingOnlyFields(UserComparisonFields.SELECT_USER_RESPONSE_TO_CREATE_USER_RESPONSE.fields())
                    .isEqualTo(user.getResponseDto());

            List<Account> customerAccounts = databaseSteps.getCustomerAccounts(user.getId());
            softly.assertThat(customerAccounts)
                    .usingRecursiveComparison()
                    .isEqualTo(actualUser.getAccounts());
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
