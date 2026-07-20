package autotesting.practice_7.ui.iteration_2;

import autotesting.practice_7.models.request.CreateUserRequestDto;
import autotesting.practice_7.models.response.CreateAccountResponseDto;
import autotesting.practice_7.pages.UserDashboardPage;
import autotesting.practice_7.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static autotesting.practice_7.testdata.AccountData.MAX_TRANSFER_AMOUNT;
import static autotesting.practice_7.testdata.AccountData.getRandomValidTransferAmount;
import static autotesting.practice_7.validation_messages.ui.AccountUiMessages.*;

public class TransferFundsTest extends BaseUiTest {

    @Test
    public void userCanTransferFundsBetweenTheirAccountsTest() {
        CreateUserRequestDto userDto = userSteps.createRandomUser();
        String token = authSteps.loginAndGetToken(userDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(token, MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(token);

        double transferAmount = getRandomValidTransferAmount();

        setAuthToken(token);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .sendTransfer(senderAccount, receiverAccount, transferAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_SUCCESSFULLY.formatted(transferAmount, receiverAccount.getAccountNumber()));

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(token, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance() - transferAmount);

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(token, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(transferAmount);
    }

    @Test
    public void userCanTransferFundsToAnotherUserAccountTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        double transferAmount = getRandomValidTransferAmount();

        setAuthToken(firstUserToken);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .sendTransfer(senderAccount, receiverAccount, transferAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_SUCCESSFULLY.formatted(transferAmount, receiverAccount.getAccountNumber()));

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance() - transferAmount);

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(transferAmount);
    }

    @Test
    public void userCannotTransferFundsWithoutSenderAccountNumberTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        double transferAmount = getRandomValidTransferAmount();

        setAuthToken(firstUserToken);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .setReceiverAccount(receiverAccount)
                .setAmount(transferAmount)
                .confirmDetails()
                .sendTransfer()
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_REQUIRED_FIELDS_NOT_FILLED);

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    public void userCannotTransferFundsWithoutReceiverAccountNumberTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        double transferAmount = getRandomValidTransferAmount();

        setAuthToken(firstUserToken);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .selectSenderAccount(senderAccount)
                .setAmount(transferAmount)
                .confirmDetails()
                .sendTransfer()
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_REQUIRED_FIELDS_NOT_FILLED);

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    public void userCannotTransferFundsWithoutAmountTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        setAuthToken(firstUserToken);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .setReceiverAccount(senderAccount)
                .setReceiverAccount(receiverAccount)
                .confirmDetails()
                .sendTransfer()
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_REQUIRED_FIELDS_NOT_FILLED);

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    public void userCannotTransferFundsWithoutConfirmTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        double transferAmount = getRandomValidTransferAmount();

        setAuthToken(firstUserToken);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .selectSenderAccount(senderAccount)
                .setReceiverAccount(receiverAccount)
                .setAmount(transferAmount)
                .sendTransfer()
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_REQUIRED_FIELDS_NOT_FILLED);

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    public void userCannotTransferFundsWithInvalidAmountTest() {
        CreateUserRequestDto firstUserDto = userSteps.createRandomUser();
        String firstUserToken = authSteps.loginAndGetToken(firstUserDto);
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(firstUserToken, MAX_TRANSFER_AMOUNT);

        CreateUserRequestDto secondUserDto = userSteps.createRandomUser();
        String secondUserToken = authSteps.loginAndGetToken(secondUserDto);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(secondUserToken);

        double transferAmount = 0.0;

        setAuthToken(firstUserToken);

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .sendTransfer(senderAccount, receiverAccount, transferAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_AMOUNT_BELOW_MIN_LIMIT);

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(firstUserToken, senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(secondUserToken, receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }
}
