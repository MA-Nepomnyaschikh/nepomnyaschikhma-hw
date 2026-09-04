package ui.iteration_2;

import api.models.response.CreateAccountResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.pages.UserDashboardPage;
import common.allure.StepLogger;
import common.annotations.Browsers;
import common.annotations.UserSession;
import common.context.TestUser;
import ui.BaseUiTest;

import java.math.BigDecimal;

import static common.testdata.factories.AccountData.MAX_TRANSFER_AMOUNT;
import static common.testdata.factories.AccountData.getRandomValidTransferAmount;
import static common.testdata.messages.ui.AccountUiMessages.*;

@DisplayName("UI. Перевод")
public class TransferFundsTest extends BaseUiTest {

    @DisplayName("UI. Пользователь может выполнить перевод между своими счетами")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(needBrowserLogin = true)
    public void userCanTransferFundsBetweenTheirAccountsTest(TestUser user) {
        BigDecimal transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount =  StepLogger.apiStep("Создать первый счет пользователя с балансом " + MAX_TRANSFER_AMOUNT, () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount =  StepLogger.apiStep("Создать второй счет пользователя", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        String alertMessage = StepLogger.uiStep("Отправить перевод", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openTransferPage()
                    .shouldBeOpened()
                    .sendTransfer(senderAccount, receiverAccount, transferAmount)
                    .getAlertMessageAndAccept();
        });

        StepLogger.uiStep("Проверить отправку перевода через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(TRANSFER_SUCCESSFULLY.formatted(transferAmount, receiverAccount.getAccountNumber()));
        });

        StepLogger.apiStep("Проверить отправку перевода через API", () -> {
            CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance().subtract(transferAmount));

            CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance().add(transferAmount));
        });
    }

    @DisplayName("UI. Пользователь может выполнить перевод на счет другого пользователя")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2, needBrowserLogin = true)
    public void userCanTransferFundsToAnotherUserAccountTest(TestUser sender, TestUser receiver) {
        BigDecimal transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount =  StepLogger.apiStep("Создать счет отправителя с балансом " + MAX_TRANSFER_AMOUNT, () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount =  StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        String alertMessage = StepLogger.uiStep("Отправить перевод", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openTransferPage()
                    .shouldBeOpened()
                    .sendTransfer(senderAccount, receiverAccount, transferAmount)
                    .getAlertMessageAndAccept();
        });

        StepLogger.uiStep("Проверить отправку перевода через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(TRANSFER_SUCCESSFULLY.formatted(transferAmount, receiverAccount.getAccountNumber()));
        });

        StepLogger.apiStep("Проверить отправку перевода через API", () -> {
            CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAccount.getBalance()).isEqualByComparingTo(senderAccount.getBalance().subtract(transferAmount));

            CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccount.getBalance()).isEqualByComparingTo(transferAmount);
        });
    }

    @DisplayName("UI. Пользователь не может выполнить перевод без указания счета отправителя")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2, needBrowserLogin = true)
    public void userCannotTransferFundsWithoutSenderAccountNumberTest(TestUser sender, TestUser receiver) {
        BigDecimal transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount =  StepLogger.apiStep("Создать счет отправителя с балансом " + MAX_TRANSFER_AMOUNT, () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount =  StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        String alertMessage = StepLogger.uiStep("Отправить перевод", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openTransferPage()
                    .shouldBeOpened()
                    .setReceiverAccount(receiverAccount)
                    .setAmount(transferAmount)
                    .confirmDetails()
                    .sendTransfer()
                    .getAlertMessageAndAccept();
        });

        StepLogger.uiStep("Проверить ошибку перевода через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(TRANSFER_REQUIRED_FIELDS_NOT_FILLED);
        });

        StepLogger.apiStep("Проверить отсутствие перевода через API", () -> {
            CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

            CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
        });
    }

    @DisplayName("UI. Пользователь не может выполнить перевод без указания счета получателя")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2, needBrowserLogin = true)
    public void userCannotTransferFundsWithoutReceiverAccountNumberTest(TestUser sender, TestUser receiver) {
        BigDecimal transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount =  StepLogger.apiStep("Создать счет отправителя с балансом " + MAX_TRANSFER_AMOUNT, () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount =  StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        String alertMessage = StepLogger.uiStep("Отправить перевод", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openTransferPage()
                    .shouldBeOpened()
                    .selectSenderAccount(senderAccount)
                    .setAmount(transferAmount)
                    .confirmDetails()
                    .sendTransfer()
                    .getAlertMessageAndAccept();
        });

        StepLogger.uiStep("Проверить ошибку перевода через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(TRANSFER_REQUIRED_FIELDS_NOT_FILLED);
        });

        StepLogger.apiStep("Проверить отсутствие перевода через API", () -> {
            CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

            CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
        });
    }

    @DisplayName("UI. Пользователь не может выполнить перевод без указания суммы")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2, needBrowserLogin = true)
    public void userCannotTransferFundsWithoutAmountTest(TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount =  StepLogger.apiStep("Создать счет отправителя с балансом " + MAX_TRANSFER_AMOUNT, () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount =  StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        String alertMessage = StepLogger.uiStep("Отправить перевод", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openTransferPage()
                    .shouldBeOpened()
                    .setReceiverAccount(senderAccount)
                    .setReceiverAccount(receiverAccount)
                    .confirmDetails()
                    .sendTransfer()
                    .getAlertMessageAndAccept();
        });

        StepLogger.uiStep("Проверить ошибку перевода через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(TRANSFER_REQUIRED_FIELDS_NOT_FILLED);
        });

        StepLogger.apiStep("Проверить отсутствие перевода через API", () -> {
            CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

            CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
        });
    }

    @DisplayName("UI. Пользователь не может выполнить перевод без подтверждения данных")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2, needBrowserLogin = true)
    public void userCannotTransferFundsWithoutConfirmTest(TestUser sender, TestUser receiver) {
        BigDecimal transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount =  StepLogger.apiStep("Создать счет отправителя с балансом " + MAX_TRANSFER_AMOUNT, () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount =  StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        String alertMessage = StepLogger.uiStep("Отправить перевод", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openTransferPage()
                    .shouldBeOpened()
                    .selectSenderAccount(senderAccount)
                    .setReceiverAccount(receiverAccount)
                    .setAmount(transferAmount)
                    .sendTransfer()
                    .getAlertMessageAndAccept();
        });

        StepLogger.uiStep("Проверить ошибку перевода через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(TRANSFER_REQUIRED_FIELDS_NOT_FILLED);
        });

        StepLogger.apiStep("Проверить отсутствие перевода через API", () -> {
            CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

            CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
        });
    }

    @DisplayName("UI. Пользователь не может выполнить перевод с невалидной суммой")
    @Test
    @Browsers(values = {"chrome"})
    @UserSession(usersCount = 2, needBrowserLogin = true)
    public void userCannotTransferFundsWithInvalidAmountTest(TestUser sender, TestUser receiver) {
        BigDecimal transferAmount = BigDecimal.valueOf(0.0);

        CreateAccountResponseDto senderAccount =  StepLogger.apiStep("Создать счет отправителя с балансом " + MAX_TRANSFER_AMOUNT, () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount =  StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        String alertMessage = StepLogger.uiStep("Отправить перевод", () -> {
            return new UserDashboardPage()
                    .open()
                    .shouldBeOpened()
                    .openTransferPage()
                    .shouldBeOpened()
                    .sendTransfer(senderAccount, receiverAccount, transferAmount)
                    .getAlertMessageAndAccept();
        });

        StepLogger.uiStep("Проверить ошибку перевода через UI", () -> {
            softly.assertThat(alertMessage).isEqualTo(TRANSFER_AMOUNT_BELOW_MIN_LIMIT);
        });

        StepLogger.apiStep("Проверить отсутствие перевода через API", () -> {
            CreateAccountResponseDto actualSenderAccount = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAccount.getBalance()).isEqualTo(senderAccount.getBalance());

            CreateAccountResponseDto actualReceiverAccount = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccount.getBalance()).isEqualTo(receiverAccount.getBalance());
        });
    }
}
