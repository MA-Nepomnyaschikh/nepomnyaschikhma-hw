package ui.iteration_2;

import models.response.CreateAccountResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.UserDashboardPage;
import supports.StepLogger;
import supports.annotations.Browsers;
import supports.annotations.UserSession;
import supports.assertions.AccountAssertions;
import supports.context.TestUser;
import ui.BaseUiTest;

import java.util.stream.Stream;

import static testdata.AccountData.getRandomValidDepositAmount;
import static testdata.expectedmessages.ui.AccountUiMessages.*;

public class DepositAccountTest extends BaseUiTest {

    @DisplayName("UI. Пользователь может пополнить свой счет с валидной суммой")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(needBrowserLogin = true)
    public void userCanDepositAccountTest(TestUser user) {
        double depositAmount = getRandomValidDepositAmount();

        CreateAccountResponseDto userAccount =  StepLogger.log("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        String alertMessage = StepLogger.log("Пополнить счет", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openDepositPage()
                    .shouldBeOpened()
                    .sendDeposit(userAccount, depositAmount)
                    .getAlertMessageAndAccept();
        });

        StepLogger.log("Проверить отправку пополнения через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(DEPOSIT_SUCCESSFULLY.formatted(depositAmount, userAccount.getAccountNumber()));
        });

        StepLogger.log("Проверить наличие пополнения через API", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
            AccountAssertions.assertDepositCompleted(softly, actualAccount, userAccount, depositAmount);
        });
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of("Сумма пополнения больше максимальной", 5000.01, DEPOSIT_AMOUNT_ABOVE_MAX_LIMIT),
                Arguments.of("Сумма пополнения равна 0", 0, DEPOSIT_AMOUNT_BELOW_MIN_LIMIT),
                Arguments.of("Сумма пополнения меньше минимальной", -0.01, DEPOSIT_AMOUNT_BELOW_MIN_LIMIT)
        );
    }

    @DisplayName("UI. Пользователь не может пополнить свой счет с невалидной суммой")
    @MethodSource("invalidAmountProvider")
    @ParameterizedTest(name = "{0}")
    @Browsers(values = {"chrome"})
    @UserSession(needBrowserLogin = true)
    public void userCannotDepositAccountWithInvalidAmountTest(String testName, double invalidAmount, String errorMessage, TestUser user) {
        CreateAccountResponseDto userAccount =  StepLogger.log("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        String alertMessage = StepLogger.log("Пополнить счет", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openDepositPage()
                    .shouldBeOpened()
                    .sendDeposit(userAccount, invalidAmount)
                    .getAlertMessageAndAccept();
        });

        StepLogger.log("Проверить ошибку пополнения через UI", () -> {
            softly.assertThat(alertMessage).contains(errorMessage);
        });

        StepLogger.log("Проверить отсутствие пополнения через API", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
            softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
            softly.assertThat(actualAccount.getTransactions()).isEmpty();
        });
    }

    @DisplayName("UI. Пользователь не может пополнить свой счет без указания номера счета")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(needBrowserLogin = true)
    public void userCannotDepositAccountWithoutAccountNumberTest(TestUser user) {
        double depositAmount = getRandomValidDepositAmount();

        CreateAccountResponseDto userAccount =  StepLogger.log("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        String alertMessage = StepLogger.log("Пополнить счет", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openDepositPage()
                    .shouldBeOpened()
                    .setAmount(depositAmount)
                    .sendDeposit()
                    .getAlertMessageAndAccept();
        });

        StepLogger.log("Проверить ошибку пополнения через UI", () -> {
            softly.assertThat(alertMessage).contains(DEPOSIT_ACCOUNT_NOT_SELECTED);
        });

        StepLogger.log("Проверить отсутствие пополнения через API", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
            softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
            softly.assertThat(actualAccount.getTransactions()).isEmpty();
        });
    }

    @DisplayName("UI. Пользователь не может пополнить свой счет без указания суммы")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(needBrowserLogin = true)
    public void userCannotDepositAccountWithoutAmountTest(TestUser user) {
        CreateAccountResponseDto userAccount =  StepLogger.log("Создать счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        String alertMessage = StepLogger.log("Пополнить счет", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openDepositPage()
                    .shouldBeOpened()
                    .selectAccount(userAccount)
                    .sendDeposit()
                    .getAlertMessageAndAccept();
        });

        StepLogger.log("Проверить ошибку пополнения через UI", () -> {
            softly.assertThat(alertMessage).contains(DEPOSIT_AMOUNT_BELOW_MIN_LIMIT);
        });

        StepLogger.log("Проверить отсутствие пополнения через API", () -> {
            CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
            softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
            softly.assertThat(actualAccount.getTransactions()).isEmpty();
        });
    }
}
