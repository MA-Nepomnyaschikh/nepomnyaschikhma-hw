package api.iteration_3;

import api.BaseTest;
import models.api.enams.TransactionStatus;
import models.api.enams.TransferStatus;
import models.api.request.TransferRequestDto;
import models.api.response.CreateAccountResponseDto;
import models.api.response.TransactionResponseDto;
import models.api.response.TransferWithFraudCheckResponseDto;
import models.db.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import supports.StepLogger;
import supports.annotations.Mock;
import supports.annotations.UserSession;
import supports.context.TestUser;

import java.math.BigDecimal;
import java.util.List;

import static models.api.enams.TransferStatus.*;
import static supports.annotations.Mock.MockScenario;
import static testdata.AccountData.*;
import static testdata.expectedmessages.api.AccountApiMessages.*;

public class TransferFundsWithFraudCheckTest extends BaseTest {

    @DisplayName("API. Авторизованный пользователь может выполнить перевод при одобрении транзакции фрод-сервисом (decision=APPROVED)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_APPROVED)
    @UserSession
    public void authorizedUserCanTransferFundsWhenTransactionApprovedByFraudServiceTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_APPROVED);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(APPROVED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance().add(transferRequestDto.getAmount()));

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getSenderAccountId());
                        softly.assertThat(actualTransaction.getId()).isPositive().isGreaterThan(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isFalse();
                    });
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance().add(transferRequestDto.getAmount()));
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance().subtract(transferRequestDto.getAmount()));

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isFalse();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance().subtract(transferRequestDto.getAmount()));
        });
    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при блокировке транзакции фрод-сервисом (decision=BLOCKED)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_BLOCKED)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenTransactionBlockedByFraudServiceTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_BLOCKED);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(BLOCKED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.BLOCKED);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isFalse();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при необходимости ручной проверки транзакции (decision=REVIEW_REQUIRED)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_REVIEW_REQUIRED_BY_DECISION)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenTransactionNeedsManualReviewTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_REQUIRES_MANUAL_REVIEW);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(MANUAL_REVIEW_REQUIRED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при необходимости ручной проверки транзакции (requiresManualReview=true)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_REVIEW_REQUIRED_BY_FLAG)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenManualReviewIsRequiredByFlagTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_REQUIRES_MANUAL_REVIEW);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(TransferStatus.MANUAL_REVIEW_REQUIRED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при необходимости дополнительной верификации транзакции (decision=VERIFICATION_REQUIRED)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_VERIFICATION_REQUIRED_BY_DECISION)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenAdditionalVerificationIsRequiredTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(ADDITIONAL_VERIFICATION_REQUIRED);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(VERIFICATION_REQUIRED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

//    @Disabled("БАГ: Если decision = APPROVED И additionalVerificationRequired = true, трансфер не переводится в REVIEW_REQUIRED")
//    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при необходимости дополнительной верификации транзакции (additionalVerificationRequired=true)")
//    @Test
//    @Mock(scenario = MockScenario.FRAUD_CHECK_VERIFICATION_REQUIRED_BY_FLAG)
//    @UserSession
//    public void authorizedUserCannotTransferFundsWhenAdditionalVerificationIsRequiredByFlagTest(TestUser user) {
//        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);
//
//        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
//            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
//        });
//        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
//            return accountSteps.createAccount(user.getToken());
//        });
//
//        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);
//
//        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
//            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
//        });
//
//        StepLogger.apiStep("Проверить перевод средств", () -> {
//            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
//            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
//            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
//            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(ADDITIONAL_VERIFICATION_REQUIRED);
//            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(VERIFICATION_REQUIRED);
//            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
//            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
//        });
//
//        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
//            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
//            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());
//
//            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
//            softly.assertThat(actualTransactions)
//                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
//                    .isEmpty();
//        });
//
//        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
//            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
//            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
//        });
//
//        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
//            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
//            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());
//
//            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
//            softly.assertThat(actualTransactions)
//                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
//                    .singleElement()
//                    .satisfies(actualTransaction -> {
//                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
//                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
//                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
//                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
//                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
//                    });
//        });
//
//        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
//            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
//            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
//        });
//    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при пустом ответе фрод-сервиса (HTTP 200, empty response)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_EMPTY_RESPONSE)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenFraudServiceReturnsEmptyResponseTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_REQUIRES_MANUAL_REVIEW);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(MANUAL_REVIEW_REQUIRED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при ошибке фрод-сервиса (HTTP 400)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_SERVICE_ERROR_400)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenFraudServiceReturnsBadRequestTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_REQUIRES_MANUAL_REVIEW);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(MANUAL_REVIEW_REQUIRED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при внутренней ошибке фрод-сервиса (HTTP 500)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_SERVICE_ERROR_500)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenFraudServiceReturnsInternalServerErrorTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_REQUIRES_MANUAL_REVIEW);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(MANUAL_REVIEW_REQUIRED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при недоступности фрод-сервиса (HTTP 503)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_SERVICE_UNAVAILABLE_503)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenFraudServiceUnavailableTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_REQUIRES_MANUAL_REVIEW);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(MANUAL_REVIEW_REQUIRED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при превышении таймаута ответа фрод-сервиса (connection error)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_TIMEOUT)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenFraudServiceTimeoutExceededTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_REQUIRES_MANUAL_REVIEW);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(MANUAL_REVIEW_REQUIRED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }

    @DisplayName("API. Авторизованный пользователь не может выполнить перевод при ошибке соединения с фрод-сервисом (connection error)")
    @Test
    @Mock(scenario = MockScenario.FRAUD_CHECK_CONNECTION_ERROR)
    @UserSession
    public void authorizedUserCannotTransferFundsWhenFraudServiceConnectionErrorTest(TestUser user) {
        BigDecimal transferAmount = BigDecimal.valueOf(10000.00);

        CreateAccountResponseDto senderAccount = StepLogger.apiStep("Создать первый счет", () -> {
            return accountSteps.createAccountWithBalance(user.getToken(), MAX_TRANSFER_AMOUNT);
        });
        CreateAccountResponseDto receiverAccount = StepLogger.apiStep("Создать второй счет", () -> {
            return accountSteps.createAccount(user.getToken());
        });

        TransferRequestDto transferRequestDto = generateTransferDto(senderAccount.getId(), receiverAccount.getId(), transferAmount);

        TransferWithFraudCheckResponseDto transferResponseDto = StepLogger.apiStep("Перевести валидную сумму с первого счета на второй", () -> {
            return accountSteps.transferWithFraudCheck(user.getToken(), transferRequestDto);
        });

        StepLogger.apiStep("Проверить перевод средств", () -> {
            softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
            softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
            softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
            softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_REQUIRES_MANUAL_REVIEW);
            softly.assertThat(transferResponseDto.getStatus()).isEqualTo(MANUAL_REVIEW_REQUIRED);
            softly.assertThat(transferResponseDto.getTransactionId()).isPositive();
            softly.assertThat(transferResponseDto.getFraudRiskScore()).isPositive();
        });

        StepLogger.apiStep("Проверить состояние второго счета через API", () -> {
            CreateAccountResponseDto actualReceiverAcc = accountSteps.getClientAccountById(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), receiverAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                    .isEmpty();
        });

        StepLogger.apiStep("Проверить состояние второго счета через БД", () -> {
            Account actualReceiverAccFromDB = databaseSteps.getCustomerAccount(user.getId(), receiverAccount.getId());
            softly.assertThat(actualReceiverAccFromDB.getBalance()).isEqualByComparingTo(receiverAccount.getBalance());
        });

        StepLogger.apiStep("Проверить состояние первого счета через API", () -> {
            CreateAccountResponseDto actualSenderAcc = accountSteps.getClientAccountById(user.getToken(), senderAccount.getId());
            softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance());

            List<TransactionResponseDto> actualTransactions = accountSteps.getAccountTransactions(user.getToken(), senderAccount.getId());
            softly.assertThat(actualTransactions)
                    .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                    .singleElement()
                    .satisfies(actualTransaction -> {
                        softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                        softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                        softly.assertThat(actualTransaction.getId()).isEqualTo(transferResponseDto.getTransactionId());
                        softly.assertThat(actualTransaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
                        softly.assertThat(actualTransaction.isFraudCheckRequired()).isTrue();
                    });
        });

        StepLogger.apiStep("Проверить состояние первого счета через БД", () -> {
            Account actualSenderAccFromDB = databaseSteps.getCustomerAccount(user.getId(), senderAccount.getId());
            softly.assertThat(actualSenderAccFromDB.getBalance()).isEqualByComparingTo(senderAccount.getBalance());
        });
    }
}
