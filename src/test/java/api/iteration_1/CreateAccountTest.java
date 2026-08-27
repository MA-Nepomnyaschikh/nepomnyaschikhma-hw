package api.iteration_1;

import api.BaseTest;
import models.api.response.CreateAccountResponseDto;
import models.api.response.ErrorResponseDto;
import models.db.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.assertions.AccountAssertions;
import supports.comparisons.AccountComparisonFields;
import supports.context.TestUser;

import java.util.List;

import static testdata.expectedmessages.api.UserApiMessages.CREATE_USER_FORBIDDEN;

@DisplayName("API. Создание счета")
public class CreateAccountTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может создать счет")
    @Test
    @UserSession
    public void authorizedUserCanCreateAccountTest(TestUser user) {
        CreateAccountResponseDto createdAccount = StepLogger.apiStep("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        StepLogger.apiStep("Проверить результат создания счета", () -> {
            AccountAssertions.assertCreateAccountResponse(softly, createdAccount);
        });

        StepLogger.apiStep("Проверить создание счета через API", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), createdAccount.getId());
            softly.assertThat(actualAccount)
                    .usingRecursiveComparison()
                    .isEqualTo(createdAccount);
        });

        StepLogger.apiStep("Проверить создание счета через БД", () -> {
            Account account = databaseSteps.getCustomerAccount(user.getId(), createdAccount.getId());
            softly.assertThat(account)
                    .usingRecursiveComparison()
                    .comparingOnlyFields(AccountComparisonFields.SELECT_ACCOUNT_RESPONSE_TO_CREATE_ACCOUNT_RESPONSE.fields())
                    .isEqualTo(createdAccount);
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может создать счет")
    @Test
    @UserSession
    public void unauthorizedUserCannotCreateAccountTest(TestUser user) {

        StepLogger.apiStep("Создать счет", () -> {
            accountSteps.createAccount(RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.apiStep("Проверить отсутствие счета через API", () -> {
            List<CreateAccountResponseDto> userAccounts = accountSteps.getClientAccounts(user.getToken());
            softly.assertThat(userAccounts).isEmpty();
        });

        StepLogger.apiStep("Проверить отсутствие счета через БД", () -> {
            List<Account> customerAccounts = databaseSteps.getCustomerAccounts(user.getId());
            softly.assertThat(customerAccounts).isEmpty();
        });
    }

    @DisplayName("API. Администратор не может создать счет")
    @Test
    public void adminCannotCreateAccountTest() {
        ErrorResponseDto errorResponse = StepLogger.apiStep("Создать счет администратором", () -> {
            return accountSteps.createAccount(
                    RequestSpecs.authAsAdmin(), ResponseSpecs.forbidden())
                    .extract().as(ErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить ошибку при создании счета", () -> {
            softly.assertThat(errorResponse.getError()).isEqualTo(CREATE_USER_FORBIDDEN);
        });
    }
}
