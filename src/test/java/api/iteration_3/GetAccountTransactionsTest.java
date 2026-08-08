package api.iteration_3;

import api.BaseTest;
import models.response.CreateAccountResponseDto;
import models.response.TransactionResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.context.TestUser;

import java.util.List;

import static testdata.AccountData.MAX_TRANSFER_AMOUNT;
import static testdata.expectedmessages.api.AccountApiMessages.GET_ACCOUNT_TRANSACTIONS_FORBIDDEN;

@DisplayName("API. Получение списка транзакций по счету")
public class GetAccountTransactionsTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может получить список транзакций по своему счету")
    @Test
    @UserSession
    public void authorizedUserCanGetOwnAccountTransactionsTest(TestUser user) {
        CreateAccountResponseDto createdAccount = StepLogger.apiStep("Создать счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });

        List<TransactionResponseDto> expectedTransactions = createdAccount.getTransactions();

        List<TransactionResponseDto> actualTransactions = StepLogger.apiStep("Получить список транзакций по счету пользователя", () -> {
            return accountSteps.getAccountTransactions(user.getToken(), createdAccount.getId());
        });

        StepLogger.apiStep("Проверить список транзакций по счету пользователя", () -> {
            softly.assertThat(actualTransactions)
                    .isEqualTo(expectedTransactions);
        });
    }

    @DisplayName("API. Авторизованный пользователь не может получить список транзакций по счету другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void authorizedUserCannotGetAnotherUserAccountTransactionsTest(TestUser firstUser, TestUser secondUser) {
        CreateAccountResponseDto createdAccount = StepLogger.apiStep("Создать счет второго пользователя", () -> {
            return accountSteps.createAccountWithBalance(secondUser.getToken(), MAX_TRANSFER_AMOUNT);
        });

        String errorResponse = StepLogger.apiStep("Получить первым пользователем список транзакций по счету второго пользователя", () -> {
            return accountSteps.getAccountTransactions( createdAccount.getId(), RequestSpecs.authAsUser(firstUser.getToken()), ResponseSpecs.forbidden())
                    .extract().asString();
        });

        StepLogger.apiStep("Проверить ошибку при получении списка транзакций", () -> {
            softly.assertThat(errorResponse).isEqualTo(GET_ACCOUNT_TRANSACTIONS_FORBIDDEN);
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может получить список транзакций по своему счету")
    @Test
    @UserSession
    public void unauthorizedUserCannotGetOwnAccountsTransactionsTest(TestUser user) {
        CreateAccountResponseDto createdAccount = StepLogger.apiStep("Создать счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });

        StepLogger.apiStep("Получить список транзакций по счету пользователя без авторизации", () -> {
            accountSteps.getAccountTransactions(createdAccount.getId(), RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });
    }

}
