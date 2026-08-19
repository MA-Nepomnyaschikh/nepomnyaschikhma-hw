package ui.iteration_2;

import models.response.CreateAccountResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.UserDashboardPage;
import supports.annotations.Browsers;
import supports.annotations.UserSession;
import supports.assertions.AccountAssertions;
import supports.context.TestUser;
import ui.BaseUiTest;

import java.util.stream.Stream;

import static testdata.AccountData.getRandomValidDepositAmount;
import static testdata.expectedmessages.ui.AccountUiMessages.*;

public class DepositAccountTest extends BaseUiTest {

    @Test
    @Browsers(values = {"chrome"})
    @UserSession
    public void userCanDepositAccountTest(TestUser user) {
        CreateAccountResponseDto userAccount = accountSteps.createAccount(user.getToken());
        double depositAmount = getRandomValidDepositAmount();

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openDepositPage()
                .shouldBeOpened()
                .sendDeposit(userAccount, depositAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(DEPOSIT_SUCCESSFULLY.formatted(depositAmount, userAccount.getAccountNumber()));

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
        AccountAssertions.assertDepositCompleted(softly, actualAccount, userAccount, depositAmount);
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of(5000.01, DEPOSIT_AMOUNT_ABOVE_MAX_LIMIT),
                Arguments.of(0, DEPOSIT_AMOUNT_BELOW_MIN_LIMIT),
                Arguments.of(-0.01, DEPOSIT_AMOUNT_BELOW_MIN_LIMIT)
        );
    }

    @MethodSource("invalidAmountProvider")
    @ParameterizedTest
    @Browsers(values = {"chrome"})
    @UserSession
    public void userCannotDepositAccountWithInvalidAmountTest(double invalidAmount, String errorMessage, TestUser user) {
        CreateAccountResponseDto userAccount = accountSteps.createAccount(user.getToken());

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openDepositPage()
                .shouldBeOpened()
                .sendDeposit(userAccount, invalidAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).contains(errorMessage);

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

    @Test
    @Browsers(values = {"chrome"})
    @UserSession
    public void userCannotDepositAccountWithoutAccountNumberTest(TestUser user) {
        CreateAccountResponseDto userAccount = accountSteps.createAccount(user.getToken());
        double depositAmount = getRandomValidDepositAmount();

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openDepositPage()
                .shouldBeOpened()
                .setAmount(depositAmount)
                .sendDeposit()
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).contains(DEPOSIT_ACCOUNT_NOT_SELECTED);

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

    @Test
    @Browsers(values = {"chrome"})
    @UserSession
    public void userCannotDepositAccountWithoutAmountTest(TestUser user) {
        CreateAccountResponseDto userAccount = accountSteps.createAccount(user.getToken());

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openDepositPage()
                .shouldBeOpened()
                .selectAccount(userAccount)
                .sendDeposit()
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).contains(DEPOSIT_AMOUNT_BELOW_MIN_LIMIT);

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(user.getToken(), userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }
}
