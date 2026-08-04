package autotesting.practice_10.testdata;

import autotesting.practice_10.models.enams.TransactionType;
import autotesting.practice_10.models.request.DepositRequestDto;
import autotesting.practice_10.models.request.TransferRequestDto;

import java.util.concurrent.ThreadLocalRandom;

public class AccountData {

    public static final String DEPOSIT = TransactionType.DEPOSIT.toString();
    public static final String TRANSFER_IN = TransactionType.TRANSFER_IN.toString();
    public static final String TRANSFER_OUT = TransactionType.TRANSFER_OUT.toString();

    public static final double MIN_DEPOSIT_AMOUNT = 0.01;
    public static final double MAX_DEPOSIT_AMOUNT = 5000.00;

    public static final double MIN_TRANSFER_AMOUNT = 0.01;
    public static final double MAX_TRANSFER_AMOUNT = 10000.00;

    public static final int NON_EXISTING_ACCOUNT_ID = Integer.MIN_VALUE;

    private AccountData() {}

    public static double getRandomValidDepositAmount() {
        return getRandomDouble(MIN_DEPOSIT_AMOUNT, MAX_DEPOSIT_AMOUNT);
    }

    public static double getRandomValidTransferAmount() {
        return getRandomDouble(MIN_TRANSFER_AMOUNT, MAX_TRANSFER_AMOUNT);
    }

    public static DepositRequestDto generateDepositDto(int accountId, double amount) {
        return DepositRequestDto.builder()
                .id(accountId)
                .balance(amount)
                .build();
    }

    public static TransferRequestDto generateTransferDto(int senderAccountId, int receiverAccountId, double amount) {
        return TransferRequestDto.builder()
                .receiverAccountId(receiverAccountId)
                .senderAccountId(senderAccountId)
                .amount(amount)
                .build();
    }

    private static double getRandomDouble(double min, double max) {
        double value = ThreadLocalRandom.current().nextDouble(min, max);
        return Math.round(value * 100.0) / 100.0;
    }

}
