package api.iteration_3;

import api.BaseTest;
import models.response.CreateAccountResponseDto;
import models.response.ErrorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.context.TestUser;

import java.util.List;

import static testdata.expectedmessages.api.UserApiMessages.CREATE_USER_FORBIDDEN;

@DisplayName("API. Получение списка счетов пользователя")
public class GetCustomerAccountsTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может получить список своих счетов")
    @Test
    @UserSession
    public void authorizedUserCanGetOwnAccountsTest(TestUser user) {
        CreateAccountResponseDto createdAccount = StepLogger.apiStep("Создать счет пользователя", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        List<CreateAccountResponseDto> expectedAccounts = List.of(createdAccount);

        List<CreateAccountResponseDto> actualAccounts = StepLogger.apiStep("Получить список счетов пользователя", () -> {
            return accountSteps.getClientAccounts(user.getToken());
        });

        StepLogger.apiStep("Проверить список счетов пользователя", () -> {
            softly.assertThat(actualAccounts)
                    .isEqualTo(expectedAccounts);
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может получить список своих счетов")
    @Test
    public void unauthorizedUserCannotGetOwnAccountsTest() {
        StepLogger.apiStep("Получить список счетов без авторизации", () -> {
            accountSteps.getClientAccounts(RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });
    }

    @DisplayName("API. Администратор не может получить список счетов")
    @Test
    public void adminCannotGetAccountsTest() {
        ErrorResponseDto errorResponse = StepLogger.apiStep("Получить список счетов администратором", () -> {
            return accountSteps.getClientAccounts(
                    RequestSpecs.authAsAdmin(), ResponseSpecs.forbidden())
                    .extract().as(ErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить ошибку при получении счетов", () -> {
            softly.assertThat(errorResponse.getError()).isEqualTo(CREATE_USER_FORBIDDEN);
        });
    }
}
