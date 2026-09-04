package api.iteration_2;

import api.BaseTest;
import api.models.request.DepositRequestDto;
import api.models.response.CreateAccountResponseDto;
import api.models.response.DepositResponseDto;
import api.models.response.TransactionResponseDto;
import api.models.response.ValidationErrorResponseDto;
import api.specs.RequestSpecs;
import api.specs.ResponseSpecs;
import common.allure.StepLogger;
import common.annotations.UserSession;
import common.assertions.AccountAssertions;
import common.comparisons.AccountComparisonFields;
import common.context.TestUser;
import database.models.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static common.testdata.factories.AccountData.*;
import static common.testdata.messages.api.AccountApiMessages.DEPOSIT_UNAUTHORIZED;

@DisplayName("API. Пополнение счета")
public class DepositAccountTest extends BaseTest {

    public static Stream<Arguments> validAmountProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(5000.00)),
                Arguments.of(BigDecimal.valueOf(4999.99)),
                Arguments.of(BigDecimal.valueOf(0.02)),
                Arguments.of(BigDecimal.valueOf(0.01))
        );
    }

    @DisplayName("API. Авторизованный пользователь может пополнить счет")
    @MethodSource("validAmountProvider")
    @ParameterizedTest(name = "Сумма пополнения: {0}")
    @UserSession
    public void authorizedUserCanDepositAccountTest(BigDecimal depositAmount, TestUser user) {
        CreateAccountResponseDto userAccount = StepLogger.apiStep("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        DepositRequestDto depositRequestDto = generateDepositDto(userAccount.getId(), depositAmount);

        DepositResponseDto depositResponseDto = StepLogger.apiStep("Пополнить счет", () -> {
            return accountSteps.deposit(user.getToken(), depositRequestDto);
        });

        StepLogger.apiStep("Проверить результат пополнения счета", () -> {
            AccountAssertions.assertDepositCompleted(softly, depositResponseDto, userAccount, depositAmount);
            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), userAccount.getId());
            AccountAssertions.assertDepositTransaction(softly, actualTransactions, depositResponseDto, depositAmount);
        });

        StepLogger.apiStep("Проверить счет после пополнения через API", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
            softly.assertThat(actualAccount)
                    .usingRecursiveComparison()
                    .isEqualTo(depositResponseDto);
        });

        StepLogger.apiStep("Проверить счет после пополнения через БД", () -> {
            Account userAccountFromDB = databaseSteps.getCustomerAccount(user.getId(), userAccount.getId());
            softly.assertThat(userAccountFromDB)
                    .usingRecursiveComparison()
                    .comparingOnlyFields(AccountComparisonFields.SELECT_ACCOUNT_RESPONSE_TO_CREATE_ACCOUNT_RESPONSE.fields())
                    .isEqualTo(depositResponseDto);
        });
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of("Сумма пополнения больше максимальной", BigDecimal.valueOf(5000.01), "Deposit amount exceeds the 5000 limit"),
                Arguments.of("Сумма пополнения равна 0", BigDecimal.valueOf(0), "Invalid account or amount"),
                Arguments.of("Сумма пополнения меньше минимальной", BigDecimal.valueOf(-0.01), "Invalid account or amount")
        );
    }

    @DisplayName("API. Авторизованный пользователь не может пополнить счет невалидной суммой")
    @MethodSource("invalidAmountProvider")
    @ParameterizedTest(name = "{0}")
    @UserSession
    public void authorizedUserCannotDepositAccountWithInvalidAmountTest(String testName, BigDecimal depositAmount, String errorMessage, TestUser user) {
        CreateAccountResponseDto userAccount = StepLogger.apiStep("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        DepositRequestDto depositRequestDto = generateDepositDto(userAccount.getId(), depositAmount);

        ValidationErrorResponseDto errorResponse = StepLogger.apiStep("Пополнить счет невалидной суммой", () -> {
            return accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.badRequest())
                    .extract().as(ValidationErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse.getMessage()).isEqualTo(errorMessage);
        });

        StepLogger.apiStep("Проверить состояние счета через API", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
            softly.assertThat(actualAccount.getBalance()).isEqualByComparingTo(userAccount.getBalance());
            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), userAccount.getId());
            softly.assertThat(actualTransactions).isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета через БД", () -> {
            Account userAccountFromDB = databaseSteps.getCustomerAccount(user.getId(), userAccount.getId());
            softly.assertThat(userAccountFromDB.getBalance()).isEqualByComparingTo(userAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может пополнить несуществующий счет")
    @Test
    @UserSession
    public void authorizedUserCannotDepositNonExistingAccountTest(TestUser user) {
        BigDecimal depositAmount = getRandomValidDepositAmount();

        DepositRequestDto depositRequestDto = generateDepositDto(NON_EXISTING_ACCOUNT_ID, depositAmount);

        ValidationErrorResponseDto errorResponse = StepLogger.apiStep("Пополнить счет без авторизации", () -> {
            return accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.forbidden())
                    .extract().as(ValidationErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse.getMessage()).isEqualTo(DEPOSIT_UNAUTHORIZED);
        });
    }

    @DisplayName("API. Авторизованный пользователь не может пополнить счет другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void authorizedUserCannotDepositAnotherUserAccountTest(TestUser firstUser, TestUser secondUser) {
        BigDecimal depositAmount = getRandomValidDepositAmount();

        CreateAccountResponseDto secondUserAccount = StepLogger.apiStep("Создать счет для второго пользователя", () -> {
            return accountSteps.createAccount(secondUser.getToken());
        });

        DepositRequestDto depositRequestDto = generateDepositDto(secondUserAccount.getId(), depositAmount);

        ValidationErrorResponseDto errorResponse = StepLogger.apiStep("Пополнить счет второго пользователя первым пользователем", () -> {
            return accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(firstUser.getToken()), ResponseSpecs.forbidden())
                    .extract().as(ValidationErrorResponseDto.class);
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse.getMessage()).isEqualTo(DEPOSIT_UNAUTHORIZED);
        });

        StepLogger.apiStep("Проверить состояние счета второго пользователя через API", () -> {
            CreateAccountResponseDto actualSecondUserAcc = accountSteps.getClientAccountById(secondUser.getToken(), secondUserAccount.getId());
            softly.assertThat(actualSecondUserAcc.getBalance()).isEqualByComparingTo(secondUserAccount.getBalance());
            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(secondUser.getToken(), secondUserAccount.getId());
            softly.assertThat(actualTransactions).isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета через БД", () -> {
            Account userAccountFromDB = databaseSteps.getCustomerAccount(secondUser.getId(), secondUserAccount.getId());
            softly.assertThat(userAccountFromDB.getBalance()).isEqualByComparingTo(secondUserAccount.getBalance());
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может пополнить счет")
    @Test
    @UserSession
    public void unauthorizedUserCannotDepositAccountTest(TestUser user) {
        BigDecimal depositAmount = getRandomValidDepositAmount();

        CreateAccountResponseDto userAccount = StepLogger.apiStep("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        DepositRequestDto depositRequestDto = generateDepositDto(userAccount.getId(), depositAmount);

        StepLogger.apiStep("Пополнить счет без авторизации", () -> {
            accountSteps.deposit(depositRequestDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.apiStep("Проверить состояние счета через API", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
            softly.assertThat(actualAccount.getBalance()).isEqualByComparingTo(userAccount.getBalance());
            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), userAccount.getId());
            softly.assertThat(actualTransactions).isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета через БД", () -> {
            Account userAccountFromDB = databaseSteps.getCustomerAccount(user.getId(), userAccount.getId());
            softly.assertThat(userAccountFromDB.getBalance()).isEqualByComparingTo(userAccount.getBalance());
        });
    }

}
