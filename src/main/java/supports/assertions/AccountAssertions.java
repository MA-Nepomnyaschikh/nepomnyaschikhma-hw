package supports.assertions;

import models.api.request.TransferRequestDto;
import models.api.response.CreateAccountResponseDto;
import models.api.response.TransferResponseDto;
import org.assertj.core.api.SoftAssertions;

import java.math.BigDecimal;

import static testdata.AccountData.*;
import static testdata.expectedmessages.api.AccountApiMessages.TRANSFER_SUCCESSFUL;
import static org.assertj.core.api.Assertions.within;

public final class AccountAssertions {

    public static void assertCreateAccountResponse(SoftAssertions softly,
                                                   CreateAccountResponseDto createdAccount) {

        softly.assertThat(createdAccount.getId()).isNotNull().isPositive();
        softly.assertThat(createdAccount.getBalance()).isZero();
        softly.assertThat(createdAccount.getAccountNumber()).isNotNull().isNotBlank();
        softly.assertThat(createdAccount.getTransactions()).isEmpty();
    }

    public static void assertDepositCompleted(SoftAssertions softly,
                                              CreateAccountResponseDto accountAfterDeposit,
                                              CreateAccountResponseDto accountBeforeDeposit,
                                              BigDecimal depositAmount) {

        softly.assertThat(accountAfterDeposit.getId()).isEqualTo(accountBeforeDeposit.getId());
        softly.assertThat(accountAfterDeposit.getAccountNumber()).isEqualTo(accountBeforeDeposit.getAccountNumber());
        softly.assertThat(accountAfterDeposit.getBalance()).isEqualTo(accountBeforeDeposit.getBalance().add(depositAmount));
        softly.assertThat(accountAfterDeposit.getTransactions())
                .singleElement()
                .satisfies(transaction -> {
                    softly.assertThat(transaction.getAmount()).isEqualByComparingTo(depositAmount);
                    softly.assertThat(transaction.getType()).isEqualTo(DEPOSIT);
                    softly.assertThat(transaction.getRelatedAccountId()).isEqualTo(accountBeforeDeposit.getId());
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
                                                    CreateAccountResponseDto senderAccount,
                                                    CreateAccountResponseDto actualSenderAcc,
                                                    TransferRequestDto transferDto) {

        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance().subtract(transferDto.getAmount()));
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .singleElement()
                .satisfies(actualTransaction -> {
                    softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferDto.getAmount());
                    softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferDto.getReceiverAccountId());
                });
    }

    public static void assertTransferInTransaction(SoftAssertions softly,
                                                   CreateAccountResponseDto receiverAccount,
                                                   CreateAccountResponseDto actualReceiverAcc,
                                                   TransferRequestDto transferDto) {

        softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance().add(transferDto.getAmount()));
        softly.assertThat(actualReceiverAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .singleElement()
                .satisfies(actualTransaction -> {
                    softly.assertThat(actualTransaction.getAmount()).isEqualByComparingTo(transferDto.getAmount());
                    softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferDto.getSenderAccountId());
                });
    }
}
