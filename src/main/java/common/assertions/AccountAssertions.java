package common.assertions;

import api.models.request.TransferRequestDto;
import api.models.response.CreateAccountResponseDto;
import api.models.response.DepositResponseDto;
import api.models.response.TransactionResponseDto;
import api.models.response.TransferResponseDto;
import org.assertj.core.api.SoftAssertions;

import java.math.BigDecimal;
import java.util.List;

import static api.models.enams.TransactionStatus.COMPLETED;
import static common.testdata.factories.AccountData.*;
import static common.testdata.messages.api.AccountApiMessages.TRANSFER_SUCCESSFUL;

public final class AccountAssertions {

    public static void assertCreateAccountResponse(SoftAssertions softly,
                                                   CreateAccountResponseDto createdAccount) {

        softly.assertThat(createdAccount.getId()).isNotNull().isPositive();
        softly.assertThat(createdAccount.getBalance()).isZero();
        softly.assertThat(createdAccount.getAccountNumber()).isNotNull().isNotBlank();
    }

    public static void assertDepositCompleted(SoftAssertions softly,
                                              DepositResponseDto depositResponse,
                                              CreateAccountResponseDto accountBeforeDeposit,
                                              BigDecimal depositAmount) {

        softly.assertThat(depositResponse.getId()).isEqualTo(accountBeforeDeposit.getId());
        softly.assertThat(depositResponse.getAccountNumber()).isEqualTo(accountBeforeDeposit.getAccountNumber());
        softly.assertThat(depositResponse.getBalance()).isEqualTo(accountBeforeDeposit.getBalance().add(depositAmount));
        softly.assertThat(depositResponse.getDepositAmount()).isEqualByComparingTo(depositAmount);
        softly.assertThat(depositResponse.getTransactionId()).isPositive();
    }

    public static void assertDepositTransaction(SoftAssertions softly,
                                              List<TransactionResponseDto> transaction,
                                              DepositResponseDto depositResponse,
                                              BigDecimal depositAmount) {

        softly.assertThat(transaction).filteredOn(actualTransaction -> actualTransaction.getType().equals(DEPOSIT))
                .singleElement()
                .satisfies(actualTransaction -> {
                    softly.assertThat(actualTransaction.getId()).isEqualTo(depositResponse.getTransactionId());
                    softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(depositResponse.getId());
                    softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(depositAmount);
                    softly.assertThat(actualTransaction.getStatus()).isEqualTo(COMPLETED);
                    softly.assertThat(actualTransaction.isFraudCheckRequired()).isFalse();
                });
    }

    public static void assertTransferCompleted(SoftAssertions softly,
                                               TransferResponseDto transferResponseDto,
                                               TransferRequestDto transferRequestDto) {

        softly.assertThat(transferResponseDto.getSenderAccountId()).isEqualTo(transferRequestDto.getSenderAccountId());
        softly.assertThat(transferResponseDto.getReceiverAccountId()).isEqualTo(transferRequestDto.getReceiverAccountId());
        softly.assertThat(transferResponseDto.getAmount()).isEqualTo(transferRequestDto.getAmount());
        softly.assertThat(transferResponseDto.getMessage()).isEqualTo(TRANSFER_SUCCESSFUL);
    }

    public static void assertTransferOutTransaction(SoftAssertions softly,
                                                    List<TransactionResponseDto> actualTransactions,
                                                    TransferResponseDto transferResponseDto) {

        softly.assertThat(actualTransactions)
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .singleElement()
                .satisfies(actualTransaction -> {
                    softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                    softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getReceiverAccountId());
                    softly.assertThat(actualTransaction.getId()).isPositive();
                    softly.assertThat(actualTransaction.getStatus()).isEqualTo(COMPLETED);
                    softly.assertThat(actualTransaction.isFraudCheckRequired()).isFalse();
                });
    }

    public static void assertTransferInTransaction(SoftAssertions softly,
                                                   List<TransactionResponseDto> actualTransactions,
                                                   TransferResponseDto transferResponseDto) {

        softly.assertThat(actualTransactions)
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .singleElement()
                .satisfies(actualTransaction -> {
                    softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferResponseDto.getAmount());
                    softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferResponseDto.getSenderAccountId());
                    softly.assertThat(actualTransaction.getId()).isPositive();
                    softly.assertThat(actualTransaction.getStatus()).isEqualTo(COMPLETED);
                    softly.assertThat(actualTransaction.isFraudCheckRequired()).isFalse();
                });
    }
}
