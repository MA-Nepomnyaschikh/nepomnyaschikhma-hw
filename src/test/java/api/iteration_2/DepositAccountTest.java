package api.iteration_2;

import api.BaseTest;
import models.request.DepositRequestDto;
import models.response.CreateAccountResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import supports.StepLogger;
import supports.annotations.UserSession;
import supports.assertions.AccountAssertions;
import supports.context.TestUser;

import java.util.stream.Stream;

import static testdata.AccountData.*;
import static testdata.expectedmessages.api.AccountApiMessages.DEPOSIT_UNAUTHORIZED;

@DisplayName("API. Пополнение счета")
public class DepositAccountTest extends BaseTest {

    public static Stream<Arguments> validAmountProvider() {
        return Stream.of(
                Arguments.of(5000.00),
                Arguments.of(4999.99),
                Arguments.of(0.02),
                Arguments.of(0.01)
        );
    }

    @DisplayName("API. Авторизованный пользователь может пополнить счет")
    @MethodSource("validAmountProvider")
    @ParameterizedTest(name = "Сумма пополнения: {0}")
    @UserSession
    public void authorizedUserCanDepositAccountTest(double depositAmount, TestUser user) {
        CreateAccountResponseDto accountBeforeDeposit = StepLogger.apiStep("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        DepositRequestDto depositRequestDto = generateDepositDto(accountBeforeDeposit.getId(), depositAmount);

        CreateAccountResponseDto accountAfterDeposit = StepLogger.apiStep("Пополнить счет", () -> {
            return accountSteps.deposit(user.getToken(), depositRequestDto);
        });

        StepLogger.apiStep("Проверить пополнение счета", () -> {
            AccountAssertions.assertDepositCompleted(softly, accountAfterDeposit, accountBeforeDeposit, depositAmount);
        });

        StepLogger.apiStep("Проверить счет после пополнения", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), accountBeforeDeposit.getId());
            softly.assertThat(actualAccount)
                    .usingRecursiveComparison()
                    .isEqualTo(accountAfterDeposit);
        });
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of("Сумма пополнения больше максимальной", 5000.01, "Deposit amount cannot exceed 5000"),
                Arguments.of("Сумма пополнения равна 0", 0, "Deposit amount must be at least 0.01"),
                Arguments.of("Сумма пополнения меньше минимальной", -0.01, "Deposit amount must be at least 0.01")
        );
    }

    @DisplayName("API. Авторизованный пользователь не может пополнить счет невалидной суммой")
    @MethodSource("invalidAmountProvider")
    @ParameterizedTest(name = "{0}")
    @UserSession
    public void authorizedUserCannotDepositAccountWithInvalidAmountTest(String testName, double depositAmount, String errorMessage, TestUser user) {
        CreateAccountResponseDto accountBeforeDeposit = StepLogger.apiStep("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        DepositRequestDto depositRequestDto = generateDepositDto(accountBeforeDeposit.getId(), depositAmount);

        String errorResponse = StepLogger.apiStep("Пополнить счет невалидной суммой", () -> {
            return accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.badRequest());
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(errorMessage);
        });

        StepLogger.apiStep("Проверить состояние счета", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), accountBeforeDeposit.getId());
            softly.assertThat(actualAccount.getBalance()).isEqualTo(accountBeforeDeposit.getBalance());
            softly.assertThat(actualAccount.getTransactions()).isEmpty();
        });
    }

    @DisplayName("API. Авторизованный пользователь не может пополнить несуществующий счет")
    @Test
    @UserSession
    public void authorizedUserCannotDepositNonExistingAccountTest(TestUser user) {
        double depositAmount = getRandomValidDepositAmount();

        DepositRequestDto depositRequestDto = generateDepositDto(NON_EXISTING_ACCOUNT_ID, depositAmount);

        String errorResponse = StepLogger.apiStep("Пополнить счет без авторизации", () -> {
            return accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.forbidden());
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(DEPOSIT_UNAUTHORIZED);
        });
    }

    @DisplayName("API. Авторизованный пользователь не может пополнить счет другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void authorizedUserCannotDepositAnotherUserAccountTest(TestUser firstUser, TestUser secondUser) {
        double depositAmount = getRandomValidDepositAmount();

        CreateAccountResponseDto secondUserAccount = StepLogger.apiStep("Создать счет для второго пользователя", () -> {
            return accountSteps.createAccount(secondUser.getToken());
        });

        DepositRequestDto depositRequestDto = generateDepositDto(secondUserAccount.getId(), depositAmount);

        String errorResponse = StepLogger.apiStep("Пополнить счет второго пользователя первым пользователем", () -> {
            return accountSteps.deposit(depositRequestDto, RequestSpecs.authAsUser(firstUser.getToken()), ResponseSpecs.forbidden());
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(DEPOSIT_UNAUTHORIZED);
        });

        StepLogger.apiStep("Проверить состояние счета", () -> {
            CreateAccountResponseDto actualSecondUserAcc = accountSteps.getClientAccountById(secondUser.getToken(), secondUserAccount.getId());
            softly.assertThat(actualSecondUserAcc.getBalance()).isEqualTo(secondUserAccount.getBalance());
            softly.assertThat(actualSecondUserAcc.getTransactions()).isEmpty();
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может пополнить счет")
    @Test
    @UserSession
    public void unauthorizedUserCannotDepositAccountTest(TestUser user) {
        double depositAmount = getRandomValidDepositAmount();

        CreateAccountResponseDto userAccount = StepLogger.apiStep("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        DepositRequestDto depositRequestDto = generateDepositDto(userAccount.getId(), depositAmount);

        StepLogger.apiStep("Пополнить счет без авторизации", () -> {
            accountSteps.deposit(depositRequestDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.apiStep("Проверить состояние счета", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
            softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
            softly.assertThat(actualAccount.getTransactions()).isEmpty();
        });
    }

}
