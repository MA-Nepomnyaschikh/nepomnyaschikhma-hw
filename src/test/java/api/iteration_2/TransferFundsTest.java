package api.iteration_2;

import api.BaseTest;
import models.api.request.TransferRequestDto;
import models.api.response.CreateAccountResponseDto;
import models.api.response.TransferResponseDto;
import models.db.Account;
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

import java.math.BigDecimal;
import java.util.stream.Stream;

import static testdata.AccountData.*;
import static testdata.expectedmessages.api.AccountApiMessages.DEPOSIT_UNAUTHORIZED;
import static testdata.expectedmessages.api.AccountApiMessages.TRANSFER_FAILED;

@DisplayName("API. Перевод")
public class TransferFundsTest extends BaseTest {

    public static Stream<Arguments> validAmountProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(10000.00)),
                Arguments.of(BigDecimal.valueOf(9999.99)),
                Arguments.of(BigDecimal.valueOf(0.02)),
                Arguments.of(BigDecimal.valueOf(0.01))
        );
    }

    @DisplayName("API. Авторизованный пользователь может перевести валидную сумму между своими счетами")
    @MethodSource("validAmountProvider")
    @ParameterizedTest(name = "Сумма перевода: {0}")
    @UserSession
    public void authorizedUserCanTransferValidAmountBetweenTheirAccountsTest(BigDecimal transferAmount, TestUser user) {
        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transfer(user.getToken(), transferDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            AccountAssertions.assertTransferCompleted(softly, transferResponseDto, transferDto);
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            AccountAssertions.assertTransferInTransaction(softly, receiverAccount, actualReceiverAcc, transferDto);
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance().add(transferDto.getAmount()));
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            AccountAssertions.assertTransferOutTransaction(softly, senderAccount, actualSenderAcc, transferDto);
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance().subtract(transferDto.getAmount()));
        });
    }

    @DisplayName("API. Авторизованный пользователь может перевести валидную сумму на счет другого пользователя")
    @MethodSource("validAmountProvider")
    @ParameterizedTest(name = "Сумма перевода: {0}")
    @UserSession(usersCount = 2)
    public void authorizedUserCanTransferValidAmountToAnotherUserAccountTest(BigDecimal transferAmount, TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму со счета отправителя на счет получателя", () -> {
            return accountSteps.transfer(sender.getToken(), transferDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
        AccountAssertions.assertTransferCompleted(softly, transferResponseDto, transferDto);
        });

        StepLogger.apiStep("Проверить состояние счета получателя через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            AccountAssertions.assertTransferInTransaction(softly, receiverAccount, actualReceiverAcc, transferDto);
        });

        StepLogger.apiStep("Проверить состояние счета получателя через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(receiver.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance().add(transferDto.getAmount()));
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            AccountAssertions.assertTransferOutTransaction(softly, senderAccount, actualSenderAcc, transferDto);
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(sender.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance().subtract(transferDto.getAmount()));
        });
    }

    public static Stream<Arguments> invalidAmountProvider() {
        return Stream.of(
                Arguments.of("Сумма перевода больше максимальной", BigDecimal.valueOf(10000.01), "Transfer amount cannot exceed 10000"),
                Arguments.of("Сумма перевода равна 0", BigDecimal.valueOf(0), "Invalid transfer: insufficient funds or invalid accounts"),
                Arguments.of("Сумма перевода меньше минимальной", BigDecimal.valueOf(-0.01), "Invalid transfer: insufficient funds or invalid accounts")
        );
    }

    @DisplayName("API. Авторизованный пользователь не может перевести невалидную сумму между своими счетами")
    @MethodSource("invalidAmountProvider")
    @ParameterizedTest(name = "{0}")
    @UserSession
    public void authorizedUserCannotTransferInvalidAmountBetweenTheirAccountsTest(String testName, BigDecimal transferAmount, String errorMessage, TestUser user) {
        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        String errorResponse = StepLogger.apiStep("Перевести невалидную сумму с первого счета на второй", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.badRequest());
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(errorMessage);
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
            softly.assertThat(actualReceiverAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
            softly.assertThat(actualSenderAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может перевести невалидную сумму на счет другого пользователя")
    @MethodSource("invalidAmountProvider")
    @ParameterizedTest(name = "{0}")
    @UserSession(usersCount = 2)
    public void authorizedUserCannotTransferInvalidAmountToAnotherUserAccountTest(String testName, BigDecimal transferAmount, String errorMessage, TestUser sender, TestUser receiver) {
        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        String errorResponse = StepLogger.apiStep("Перевести невалидную сумму со счета отправителя на счет получателя", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(sender.getToken()), ResponseSpecs.badRequest());
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(errorMessage);
        });

        StepLogger.apiStep("Проверить состояние счета получателя через API", () -> {
        CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
        softly.assertThat(actualReceiverAcc.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        softly.assertThat(actualReceiverAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета получателя через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(receiver.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через API", () -> {
        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(sender.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может перевести сумму превышающую баланс на счет другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void authorizedUserCannotTransferAmountExceedingAccountBalanceTest(TestUser sender, TestUser receiver) {
        BigDecimal transferAmountExceedingBalance = BigDecimal.valueOf(MAX_DEPOSIT_AMOUNT.doubleValue() + 0.01);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_DEPOSIT_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmountExceedingBalance);

        String errorResponse = StepLogger.apiStep("Перевести сумму превышающую баланс со счета отправителя на счет получателя", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(sender.getToken()), ResponseSpecs.badRequest());
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(TRANSFER_FAILED);
        });

        StepLogger.apiStep("Проверить состояние счета получателя через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
            softly.assertThat(actualReceiverAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета получателя через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(receiver.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
            softly.assertThat(actualSenderAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(sender.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может перевести валидную сумму на несуществующий счет")
    @Test
    @UserSession
    public void authorizedUserCannotTransferFundsIntoNonExistingAccountTest(TestUser user) {
        BigDecimal transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), NON_EXISTING_ACCOUNT_ID, transferAmount);


        String errorResponse = StepLogger.apiStep("Перевести валидную сумму со счета отправителя на несуществующий счет", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(user.getToken()), ResponseSpecs.badRequest());
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(TRANSFER_FAILED);
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через API", () -> {
        CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
        softly.assertThat(actualSenderAcc.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может перевести валидную сумму с несуществующего счета на счет другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void authorizedUserCannotTransferFundsFromNonExistingAccountTest(TestUser sender, TestUser receiver) {
        BigDecimal transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(NON_EXISTING_ACCOUNT_ID, receiverAccount.getId(), transferAmount);


        String errorResponse = StepLogger.apiStep("Перевести валидную сумму с несуществующего счета на счет получателя", () -> {
            return accountSteps.transfer(transferDto, RequestSpecs.authAsUser(sender.getToken()), ResponseSpecs.forbidden());
        });

        StepLogger.apiStep("Проверить сообщение об ошибке", () -> {
            softly.assertThat(errorResponse).isEqualTo(DEPOSIT_UNAUTHORIZED);
        });

        StepLogger.apiStep("Проверить состояние счета получателя через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
            softly.assertThat(actualReceiverAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета получателя через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(receiver.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
            softly.assertThat(actualSenderAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(sender.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Неавторизованный пользователь не может перевести валидную сумму на счет другого пользователя")
    @Test
    @UserSession(usersCount = 2)
    public void unauthorizedUserCannotTransferFundsTest(TestUser sender, TestUser receiver) {
        BigDecimal transferAmount = getRandomValidTransferAmount();

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать счет отправителя", () -> {
            return accountSteps.createAccountWithBalance(sender.getToken(), MAX_TRANSFER_AMOUNT);
        });

        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать счет получателя", () -> {
            return accountSteps.createAccount(receiver.getToken());
        });

        TransferRequestDto transferDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        StepLogger.apiStep("Перевести валидную сумму со счета отправителя на счет получателя без авторизации", () -> {
            accountSteps.transfer(transferDto, RequestSpecs.unauth(), ResponseSpecs.unauthorized());
        });

        StepLogger.apiStep("Проверить состояние счета получателя через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(receiver.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
            softly.assertThat(actualReceiverAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета получателя через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(receiver.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(sender.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
            softly.assertThat(actualSenderAcc.getTransactions())
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние счета отправителя через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(sender.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }
}