package autotesting.practice_9.supports.assertions;

import autotesting.practice_9.models.request.TransferRequestDto;
import autotesting.practice_9.models.response.CreateAccountResponseDto;
import autotesting.practice_9.models.response.TransferResponseDto;
import org.assertj.core.api.SoftAssertions;

import static autotesting.practice_9.testdata.AccountData.*;
import static autotesting.practice_9.testdata.expectedmessages.api.AccountApiMessages.TRANSFER_SUCCESSFUL;
import static org.assertj.core.api.Assertions.within;

public final class AccountAssertions {

    public static void assertAccountCreated(SoftAssertions softly,
                                            CreateAccountResponseDto createdAccount) {

        softly.assertThat(createdAccount.getId()).isNotNull().isPositive();
        softly.assertThat(createdAccount.getBalance()).isZero();
        softly.assertThat(createdAccount.getAccountNumber()).isNotNull().isNotBlank();
        softly.assertThat(createdAccount.getTransactions()).isEmpty();
    }

    public static void assertDepositCompleted(SoftAssertions softly,
                                              CreateAccountResponseDto accountAfterDeposit,
                                              CreateAccountResponseDto accountBeforeDeposit,
                                              double depositAmount) {

        softly.assertThat(accountAfterDeposit.getId()).isEqualTo(accountBeforeDeposit.getId());
        softly.assertThat(accountAfterDeposit.getAccountNumber()).isEqualTo(accountBeforeDeposit.getAccountNumber());
        softly.assertThat(accountAfterDeposit.getBalance()).isEqualTo(accountBeforeDeposit.getBalance() + depositAmount);
        softly.assertThat(accountAfterDeposit.getTransactions())
                .singleElement()
                .satisfies(transaction -> {
                    softly.assertThat(transaction.getAmount()).isEqualTo(depositAmount);
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

        softly.assertThat(actualSenderAcc.getBalance()).isEqualTo(senderAccount.getBalance() - transferDto.getAmount(), within(0.00001));
        softly.assertThat(actualSenderAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_OUT))
                .singleElement()
                .satisfies(actualTransaction -> {
                    softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferDto.getAmount());
                    softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferDto.getReceiverAccountId());
                });
    }

    public static void assertTransferInTransaction(SoftAssertions softly,
                                                   CreateAccountResponseDto receiverAccount,
                                                   CreateAccountResponseDto actualReceiverAcc,
                                                   TransferRequestDto transferDto) {

        softly.assertThat(actualReceiverAcc.getBalance()).isEqualTo(receiverAccount.getBalance() + transferDto.getAmount(), within(0.00001));
        softly.assertThat(actualReceiverAcc.getTransactions())
                .filteredOn(actualTransaction -> actualTransaction.getType().equals(TRANSFER_IN))
                .singleElement()
                .satisfies(actualTransaction -> {
                    softly.assertThat(actualTransaction.getAmount()).isEqualTo(transferDto.getAmount());
                    softly.assertThat(actualTransaction.getRelatedAccountId()).isEqualTo(transferDto.getSenderAccountId());
                });
    }
}
