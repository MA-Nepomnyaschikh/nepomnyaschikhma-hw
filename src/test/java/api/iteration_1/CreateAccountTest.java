package api.iteration_1;

import api.BaseTest;
import models.response.CreateAccountResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.assertions.AccountAssertions;
import supports.context.TestUser;

import java.util.List;

public class CreateAccountTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может создать аккаунт")
    @Test
    @UserSession
    public void authorizedUserCanCreateAccountTest(TestUser user) {
        CreateAccountResponseDto createdAccount = StepLogger.log("Создать аккаунт", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        StepLogger.log("Проверить создание аккаунта", () -> {
            AccountAssertions.assertAccountCreated(softly, createdAccount);
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), createdAccount.getId());
            softly.assertThat(actualAccount)
                    .usingRecursiveComparison()
                    .isEqualTo(createdAccount);
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может создать аккаунт")
    @Test
    @UserSession
    public void unauthorizedUserCannotCreateAccountTest(TestUser user) {

        StepLogger.log("Создать аккаунт", () -> {
            accountSteps.createAccount(RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.log("Проверить отсутствие аккаунта", () -> {
            List<CreateAccountResponseDto> userAccounts = accountSteps.getClientAccounts(user.getToken());
            softly.assertThat(userAccounts).isEmpty();
        });
    }
}
