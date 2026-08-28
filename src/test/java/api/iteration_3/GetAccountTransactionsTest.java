package api.iteration_3;

import api.BaseTest;
import api.models.request.DepositRequestDto;
import api.models.request.TransferRequestDto;
import api.models.response.*;
import database.models.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import common.allure.StepLogger;
import common.annotations.UserSession;
import common.comparisons.TransactionComparisonFields;
import common.context.TestUser;

import java.math.BigDecimal;
import java.util.List;

import static common.testdata.factories.AccountData.*;
import static common.testdata.messages.api.AccountApiMessages.GET_ACCOUNT_TRANSACTIONS_FORBIDDEN;

@DisplayName("API. Получение списка транзакций по счету")
public class GetAccountTransactionsTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может получить список транзакций по своему счету")
    @Test
    @UserSession
    public void authorizedUserCanGetOwnAccountTransactionsTest(TestUser user) {
        BigDecimal depositAmount = MAX_DEPOSIT_AMOUNT;
        BigDecimal transferAmount = MAX_DEPOSIT_AMOUNT;

        CreateAccountResponseDto firstAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        CreateAccountResponseDto secondAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        DepositRequestDto depositRequestDto = generateDepositDto(firstAccount.getId(), depositAmount);
        DepositResponseDto depositResponseDto = StepLogger.apiStep("Создать транзакцию пополнения", () -> {
            return accountSteps.deposit(user.getToken(), depositRequestDto);
        });

        TransferRequestDto transferRequestDto = generateTransferDto(firstAccount.getId(), secondAccount.getId(), transferAmount);
        TransferResponseDto transferResponseDto = StepLogger.apiStep("Создать транзакцию перевода", () -> {
            return accountSteps.transfer(user.getToken(), transferRequestDto);
        });

        List<TransactionResponseDto> actualTransactions = StepLogger.apiStep("Получить список транзакций по счету пользователя", () -> {
            return accountSteps.getAccountTransactions(user.getToken(), firstAccount.getId());
        });

        StepLogger.apiStep("Проверить созданную транзакцию по счету пользователя через API", () -> {
            softly.assertThat(actualTransactions)
                    .satisfiesExactlyInAnyOrder(
                            deposit -> {
                                softly.assertThat(deposit.getType()).isEqualTo(DEPOSIT);
                                softly.assertThat(deposit.getAmount()).isEqualByComparingTo(depositAmount);
                                softly.assertThat(deposit.getId()).isEqualTo(depositResponseDto.getTransactionId());
                                softly.assertThat(deposit.getRelatedAccountId()).isEqualTo(firstAccount.getId());

                            },
                            transfer -> {
                                softly.assertThat(transfer.getType()).isEqualTo(TRANSFER_OUT);
                                softly.assertThat(transfer.getAmount()).isEqualByComparingTo(transferAmount);
                                softly.assertThat(transfer.getRelatedAccountId()).isEqualTo(secondAccount.getId());
                            }
                    );
        });

        StepLogger.apiStep("Проверить список транзакций по счету пользователя через БД", () -> {
            List<Transaction> actualTransactionsFromDb = databaseSteps.getAccountTransactions(firstAccount.getId());

            softly.assertThat(actualTransactionsFromDb)
                    .usingRecursiveFieldByFieldElementComparatorOnFields(TransactionComparisonFields.SELECT_ACCOUNT_TRANSACTIONS_TO_CREATE_ACCOUNT_RESPONSE.fields())
                    .isEqualTo(actualTransactions);
        });
    }

    @DisplayName("API. Авторизованный пользователь не может получить список транзакций по счету другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void authorizedUserCannotGetAnotherUserAccountTransactionsTest(TestUser firstUser, TestUser secondUser) {
        CreateAccountResponseDto createdAccount = StepLogger.apiStep("Создать счет второго пользователя", () -> {
            return accountSteps.createAccountWithBalance(secondUser.getToken(), MAX_TRANSFER_AMOUNT);
        });

        ValidationErrorResponseDto errorResponse = StepLogger.apiStep("Получить первым пользователем список транзакций по счету второго пользователя", () -> {
            return accountSteps.getAccountTransactions(createdAccount.getId(), RequestSpecs.authAsUser(firstUser.getToken()), ResponseSpecs.forbidden())
                    .extract().as(ValidationErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить ошибку при получении списка транзакций", () -> {
            softly.assertThat(errorResponse.getMessage()).isEqualTo(GET_ACCOUNT_TRANSACTIONS_FORBIDDEN);
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
