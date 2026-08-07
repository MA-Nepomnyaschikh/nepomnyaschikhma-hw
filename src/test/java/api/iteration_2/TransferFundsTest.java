package api.iteration_2;

import api.BaseTest;
import models.request.TransferRequestDto;
import models.response.CreateAccountResponseDto;
import models.response.TransferResponseDto;
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
import static testdata.expectedmessages.api.AccountApiMessages.TRANSFER_FAILED;

@DisplayName("API. Перевод")
public class TransferFundsTest extends BaseTest {

    public static Stream<Arguments> validAmountProvider() {
        return Stream.of(
                Arguments.of(10000.00),
                Arguments.of(9999.99),
                Arguments.of(0.02),
                Arguments.of(0.01)
        );
    }

    @DisplayName("API. Авторизованный пользователь может перевести валидную сумму между своими счетами")
    @MethodSource("validAmountProvider")
    @ParameterizedTest(name = "Сумма перевода: {0}")
    @UserSession
    public void authorizedUserCanTransferValidAmountBetweenTheirAccountsTest(double transferAmount, TestUser user) {
        CreateAccountResponseDto senderAccount = StepLogger.log("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.log("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferResponseDto transferResponseDto = StepLogger.log("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transfer(user.getToken(), transferDto);
        });

        StepLogger.log("Проверить перевод средств", () -> {
            AccountAssertions.assertTransferCompleted(softly, transferResponseDto, transferDto);
        });

        StepLogger.log("Проверить состояние второго счета", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            AccountAssertions.assertTransferInTransaction(softly, receiverAccount, actualReceiverAcc, transferDto);
        });

        StepLogger.log("Проверить состояние первого счета", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            AccountAssertions.assertTransferOutTransaction(softly, senderAccount, actualSenderAcc, transferDto);
        });
    }

    @DisplayName("API. Авторизованный пользователь может перевести валидную сумму на счет другого пользователя")
    @MethodSource("validAmountProvider")
    @ParameterizedTest(name = "Сумма перевода: {0}")
    @UserSession(usersCount = 2)
    public void authorizedUserCanTransferValidAmountToAnotherUserAccountTest(double transferAmount, TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = StepLogger.log("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.log("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferResponseDto transferResponseDto = StepLogger.log("Перевести валидную сумму со счета отправителя на счет получателя", () -> {
            return accountSteps.transfer(sender.getToken(), transferDto);
        });

        StepLogger.log("Проверить перевод средств", () -> {
        AccountAssertions.assertTransferCompleted(softly, transferResponseDto, transferDto);
        });

        StepLogger.log("Проверить состояние счета получателя", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            AccountAssertions.assertTransferInTransaction(softly, receiverAccount, actualReceiverAcc, transferDto);
        });

        StepLogger.log("Проверить состояние счета отправителя", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            AccountAssertions.assertTransferOutTransaction(softly, senderAccount, actualSenderAcc, transferDto);
        });
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of("Сумма перевода больше максимальной", 10000.01, "Transfer amount cannot exceed 10000"),
                Arguments.of("Сумма перевода равна 0", 0, "Transfer amount must be at least 0.01"),
                Arguments.of("Сумма перевода меньше минимальной", -0.01, "Transfer amount must be at least 0.01")
        );
    }

    @DisplayName("API. Авторизованный пользователь не может перевести невалидную сумму между своими счетами")
    @MethodSource("invalidAmountProvider")
    @ParameterizedTest(name = "{0}")
    @UserSession
    public void authorizedUserCannotTransferInvalidAmountBetweenTheirAccountsTest(String testName, double transferAmount, String errorMessage, TestUser user) {
        CreateAccountResponseDto senderAccount = StepLogger.log("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.log("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        String errorResponse = StepLogger.log("Перевести невалидную сумму с первого счета на второй", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.badRequest());
        });

        StepLogger.log("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(errorMessage);
        });

        StepLogger.log("Проверить состояние второго счета", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
            softly.assertThat(actualReceiverAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.log("Проверить состояние первого счета", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
            softly.assertThat(actualSenderAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .isEmpty();
        });
    }

    @DisplayName("API. Авторизованный пользователь не может перевести невалидную сумму на счет другого пользователя")
    @MethodSource("invalidAmountProvider")
    @ParameterizedTest(name = "{0}")
    @UserSession(usersCount = 2)
    public void authorizedUserCannotTransferInvalidAmountToAnotherUserAccountTest(String testName, double transferAmount, String errorMessage, TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = StepLogger.log("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.log("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        String errorResponse = StepLogger.log("Перевести невалидную сумму со счета отправителя на счет получателя", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(sender.getToken()), ResponseSpecs.badRequest());
        });

        StepLogger.log("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(errorMessage);
        });

        StepLogger.log("Проверить состояние счета получателя", () -> {
        CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
        softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
        softly.assertThat(actualReceiverAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .isEmpty();
        });

        StepLogger.log("Проверить состояние счета отправителя", () -> {
        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
        });
    }

    @DisplayName("API. Авторизованный пользователь не может перевести сумму превышающую баланс на счет другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void authorizedUserCannotTransferAmountExceedingAccountBalanceTest(TestUser sender, TestUser receiver) {
        double transferAmountExceedingBalance = MAX_DEPOSIT_AMOUNT + 0.01;

        CreateAccountResponseDto senderAccount = StepLogger.log("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_DEPOSIT_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.log("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmountExceedingBalance);

        String errorResponse = StepLogger.log("Перевести сумму превышающую баланс со счета отправителя на счет получателя", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(sender.getToken()), ResponseSpecs.badRequest());
        });

        StepLogger.log("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(TRANSFER_FAILED);
        });

        StepLogger.log("Проверить состояние счета получателя", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
            softly.assertThat(actualReceiverAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.log("Проверить состояние счета отправителя", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
            softly.assertThat(actualSenderAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .isEmpty();
        });
    }

    @DisplayName("API. Авторизованный пользователь не может перевести валидную сумму на несуществующий счет")
    @Test
    @UserSession
    public void authorizedUserCannotTransferFundsIntoNonExistingAccountTest(TestUser user) {
        double transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount = StepLogger.log("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), NON_EXISTING_ACCOUNT_ID, transferAmount);


        String errorResponse = StepLogger.log("Перевести валидную сумму со счета отправителя на несуществующий счет", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.badRequest());
        });

        StepLogger.log("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(TRANSFER_FAILED);
        });

        StepLogger.log("Проверить состояние счета отправителя", () -> {
        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
        });
    }

    @DisplayName("API. Авторизованный пользователь не может перевести валидную сумму с несуществующего счета на счет другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void authorizedUserCannotTransferFundsFromNonExistingAccountTest(TestUser sender, TestUser receiver) {
        double transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount = StepLogger.log("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.log("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(NON_EXISTING_ACCOUNT_ID, receiverAccount.getId(), transferAmount);


        String errorResponse = StepLogger.log("Перевести валидную сумму с несуществующего счета на счет получателя", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(sender.getToken()), ResponseSpecs.forbidden());
        });

        StepLogger.log("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(DEPOSIT_UNAUTHORIZED);
        });

        StepLogger.log("Проверить состояние счета получателя", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
            softly.assertThat(actualReceiverAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.log("Проверить состояние счета отправителя", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
            softly.assertThat(actualSenderAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .isEmpty();
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может перевести валидную сумму на счет другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void unauthorizedUserCannotTransferFundsTest(TestUser sender, TestUser receiver) {
        double transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount = StepLogger.log("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.log("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        StepLogger.log("Перевести валидную сумму со счета отправителя на счет получателя без авторизации", () -> {
            accountSteps.transfer(transferDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.log("Проверить состояние счета получателя", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
            softly.assertThat(actualReceiverAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.log("Проверить состояние счета отправителя", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
            softly.assertThat(actualSenderAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .isEmpty();
        });
    }
}