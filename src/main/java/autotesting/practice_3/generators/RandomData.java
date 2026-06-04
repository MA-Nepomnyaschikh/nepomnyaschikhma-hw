package autotesting.practice_3.generators;

import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class RandomData {

    private static final Faker FAKER = new Faker();

    private RandomData() {}

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
}
