package autotesting.practice_8.ui.iteration_2;

import autotesting.practice_8.models.response.CreateAccountResponseDto;
import autotesting.practice_8.pages.UserDashboardPage;
import autotesting.practice_8.supports.annotations.Browsers;
import autotesting.practice_8.supports.annotations.UserSession;
import autotesting.practice_8.supports.context.TestUser;
import autotesting.practice_8.ui.BaseUiTest;
import org.junit.jupiter.api.Test;

import static autotesting.practice_8.testdata.AccountData.MAX_TRANSFER_AMOUNT;
import static autotesting.practice_8.testdata.AccountData.getRandomValidTransferAmount;
import static autotesting.practice_8.testdata.expectedmessages.ui.AccountUiMessages.*;

public class TransferFundsTest extends BaseUiTest {

    @Test
    @Browsers(values = {"chrome"})
    @UserSession
    public void userCanTransferFundsBetweenTheirAccountsTest(TestUser user) {
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(user.getToken());
        double transferAmount = getRandomValidTransferAmount();

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .sendTransfer(senderAccount, receiverAccount, transferAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_SUCCESSFULLY.formatted(transferAmount, receiverAccount.getAccountNumber()));

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance() - transferAmount);

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance() + transferAmount);
    }

    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2)
    public void userCanTransferFundsToAnotherUserAccountTest(TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(receiver.getToken());
        double transferAmount = getRandomValidTransferAmount();

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .sendTransfer(senderAccount, receiverAccount, transferAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_SUCCESSFULLY.formatted(transferAmount, receiverAccount.getAccountNumber()));

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance() - transferAmount);

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(transferAmount);
    }

    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2)
    public void userCannotTransferFundsWithoutSenderAccountNumberTest(TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(receiver.getToken());
        double transferAmount = getRandomValidTransferAmount();

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

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2)
    public void userCannotTransferFundsWithoutReceiverAccountNumberTest(TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(receiver.getToken());
        double transferAmount = getRandomValidTransferAmount();

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

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2)
    public void userCannotTransferFundsWithoutAmountTest(TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(receiver.getToken());

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

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2)
    public void userCannotTransferFundsWithoutConfirmTest(TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(receiver.getToken());
        double transferAmount = getRandomValidTransferAmount();

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

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }

    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2)
    public void userCannotTransferFundsWithInvalidAmountTest(TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        CreateAccountResponseDto receiverAccount = accountSteps.createAccount(receiver.getToken());
        double transferAmount = 0.0;

        String alertMessage = new UserDashboardPage()
                .open()
                .shouldBeOpened()
                .openTransferPage()
                .shouldBeOpened()
                .sendTransfer(senderAccount, receiverAccount, transferAmount)
                .getAlertMessageAndAccept();

        softly.assertThat(alertMessage).isEqualTo(TRANSFER_AMOUNT_BELOW_MIN_LIMIT);

        CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

        CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
        softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
    }
}
