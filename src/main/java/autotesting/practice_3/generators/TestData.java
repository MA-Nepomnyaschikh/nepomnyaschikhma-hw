package autotesting.practice_3.generators;

import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestData {

    private static final Faker FAKER = new Faker();

    public static final String ADMIN_LOGIN = "admin";
    public static final String ADMIN_PASSWORD = "admin";

    public static final int NON_EXISTING_ACCOUNT_ID = Integer.MIN_VALUE;

    public static final double MIN_DEPOSIT_AMOUNT = 0.01;
    public static final double MAX_DEPOSIT_AMOUNT = 5000.00;

    public static final double MIN_TRANSFER_AMOUNT = 0.01;
    public static final double MAX_TRANSFER_AMOUNT = 10000.00;

    private TestData() {}

    public static String getUsername() {
        return "User_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public static String getPassword() {
        String upper = RandomStringUtils.secure().next(1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        String lower = RandomStringUtils.secure().next(1, "abcdefghijklmnopqrstuvwxyz");
        String digit = RandomStringUtils.secure().next(1, "0123456789");
        String special = RandomStringUtils.secure().next(1, "!@#$%^&");

        String other = RandomStringUtils.secure()
                .next(8, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*");

        return upper + lower + digit + special + other;
    }

    public static String getName() {
        return FAKER.name().firstName() + " " + FAKER.name().lastName();
    }

    public static double getRandomValidTransferAmount() {
        return getRandomDouble(MIN_TRANSFER_AMOUNT, MAX_TRANSFER_AMOUNT);
    }

    public static double getRandomValidDepositAmount() {
        return getRandomDouble(MIN_DEPOSIT_AMOUNT, MAX_DEPOSIT_AMOUNT);
    }

    public static double getRandomDouble(double min, double max) {
        double value = ThreadLocalRandom.current().nextDouble(min, max);
        return Math.round(value * 100.0) / 100.0;
    }
}
