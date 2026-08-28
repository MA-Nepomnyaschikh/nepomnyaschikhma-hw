package api.iteration_3;

import api.BaseTest;
import api.models.response.CreateAccountResponseDto;
import api.models.response.ErrorResponseDto;
import database.models.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import common.allure.StepLogger;
import common.annotations.UserSession;
import common.comparisons.AccountComparisonFields;
import common.context.TestUser;

import java.util.List;

import static common.testdata.messages.api.UserApiMessages.CREATE_USER_FORBIDDEN;

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

        StepLogger.apiStep("Проверить список счетов пользователя через API", () -> {
            softly.assertThat(actualAccounts)
                    .isEqualTo(expectedAccounts);
        });

        StepLogger.apiStep("Проверить список счетов пользователя через БД", () -> {
            List<Account> actualAccountsFromDb = databaseSteps.getCustomerAccounts(user.getId());

            softly.assertThat(actualAccountsFromDb)
                    .usingRecursiveFieldByFieldElementComparatorOnFields(AccountComparisonFields.SELECT_ACCOUNT_RESPONSE_TO_CREATE_ACCOUNT_RESPONSE.fields())
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
