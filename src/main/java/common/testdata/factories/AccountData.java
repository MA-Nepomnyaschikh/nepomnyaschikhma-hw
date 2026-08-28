package common.testdata.factories;

import api.models.enams.TransactionType;
import api.models.request.DepositRequestDto;
import api.models.request.TransferRequestDto;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class AccountData {

    public static final String DEPOSIT = TransactionType.DEPOSIT.toString();
    public static final String TRANSFER_IN = TransactionType.TRANSFER_IN.toString();
    public static final String TRANSFER_OUT = TransactionType.TRANSFER_OUT.toString();

    public static final BigDecimal MIN_DEPOSIT_AMOUNT = BigDecimal.valueOf(0.01);
    public static final BigDecimal MAX_DEPOSIT_AMOUNT = BigDecimal.valueOf(5000.00);

    public static final BigDecimal MIN_TRANSFER_AMOUNT = BigDecimal.valueOf(0.01);
    public static final BigDecimal MAX_TRANSFER_AMOUNT = BigDecimal.valueOf(10000.00);

    public static final int NON_EXISTING_ACCOUNT_ID = Integer.MIN_VALUE;

    private AccountData() {}

    public static BigDecimal getRandomValidDepositAmount() {
        double amount = getRandomDouble(MIN_DEPOSIT_AMOUNT, MAX_DEPOSIT_AMOUNT);
        return BigDecimal.valueOf(Math.round(amount * 100.0) / 100.0);
    }

    public static BigDecimal getRandomValidTransferAmount() {
        double amount = getRandomDouble(MIN_TRANSFER_AMOUNT, MAX_TRANSFER_AMOUNT);
        return BigDecimal.valueOf(Math.round(amount * 100.0) / 100.0);
    }

    public static DepositRequestDto generateDepositDto(long accountId, BigDecimal amount) {
        return DepositRequestDto.builder()
                .accountId(accountId)
                .amount(amount)
                .build();
    }

    public static TransferRequestDto generateTransferDto(long senderAccountId, long receiverAccountId, BigDecimal amount) {
        return TransferRequestDto.builder()
                .receiverAccountId(receiverAccountId)
                .senderAccountId(senderAccountId)
                .amount(amount)
                .build();
    }

    private static double getRandomDouble(BigDecimal min, BigDecimal max) {
        double value = ThreadLocalRandom.current().nextDouble(min.doubleValue(), max.doubleValue());
        return Math.round(value * 100.0) / 100.0;
    }

}
