package autotesting.practice_7.ui.iteration_2;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.models.response.CreateAccountResponseDto;
import autotesting.practice_7.pages.UserDashboardPage;
import autotesting.practice_7.ui.BaseUiTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static autotesting.practice_7.testdata.AccountData.DEPOSIT;
import static autotesting.practice_7.testdata.AccountData.getRandomValidDepositAmount;
import static autotesting.practice_7.validation_messages.ui.AccountUiMessages.*;

public class DepositAccountTest extends BaseUiTest {

    @Test
    public void userCanDepositAccountTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto userAccount = accountSteps.createAccount(token);
        double depositAmount = getRandomValidDepositAmount();
        setAuthToken(token);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openDepositPage()
                .shouldBeOpened()
                .sendDeposit(userAccount, depositAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(DEPOSIT_SUCCESSFULLY.formatted(depositAmount, userAccount.getAccountNumber()));

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(token, userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(depositAmount);
        softly.assertThat(actualAccount.getTransactions())
                .singleElement()
                .satisfies(transaction -> {
                    softly.assertThat(transaction.getAmount()).isEqualTo(depositAmount);
                    softly.assertThat(transaction.getType()).isEqualTo(DEPOSIT);
                    softly.assertThat(transaction.getRelatedAccountId()).isEqualTo(userAccount.getId());
                });
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
    public void userCannotDepositAccountWithInvalidAmountTest(double invalidAmount, String errorMessage) {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto userAccount = accountSteps.createAccount(token);
        setAuthToken(token);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openDepositPage()
                .shouldBeOpened()
                .sendDeposit(userAccount, invalidAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).contains(errorMessage);

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(token, userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

    @Test
    public void userCannotDepositAccountWithoutAccountNumberTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto userAccount = accountSteps.createAccount(token);
        double depositAmount = getRandomValidDepositAmount();
        setAuthToken(token);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openDepositPage()
                .shouldBeOpened()
                .setAmount(depositAmount)
                .sendDeposit()
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).contains(DEPOSIT_ACCOUNT_NOT_SELECTED);

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(token, userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }

    @Test
    public void userCannotDepositAccountWithoutAmountTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto userAccount = accountSteps.createAccount(token);
        setAuthToken(token);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openDepositPage()
                .shouldBeOpened()
                .selectAccount(userAccount)
                .sendDeposit()
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).contains(DEPOSIT_AMOUNT_BELOW_MIN_LIMIT);

        CreateAccountResponseDto actualAccount = accountSteps.getClientAccountById(token, userAccount.getId());
        softly.assertThat(actualAccount.getBalance()).isEqualTo(userAccount.getBalance());
        softly.assertThat(actualAccount.getTransactions()).isEmpty();
    }
}
