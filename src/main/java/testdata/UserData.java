package testdata;

import models.enams.UserRole;
import models.request.CreateUserRequestDto;
import models.request.UpdateUserRequestDto;
import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class UserData {

    private static final Faker FAKER = new Faker();
    public static final String USER_ROLE = UserRole.USER.toString();
    public static final String ADMIN_ROLE = UserRole.ADMIN.toString();

    private UserData() {}

    public static String getUsername() {
        return "User_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public static String getInvalidUsername() {
        return "User@" + UUID.randomUUID().toString().substring(0, 8);
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

    public static String getValidName() {
        String firstName = FAKER.name()
                .firstName()
                .replaceAll("[^A-Za-z]", "");

        String lastName = FAKER.name()
                .lastName()
                .replaceAll("[^A-Za-z]", "");

        return firstName + " " + lastName;
    }

    public static CreateUserRequestDto generateUserDto(String username, String password, String role) {
        return CreateUserRequestDto.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

    public static UpdateUserRequestDto generateUpdateUserDto(String newName) {
        return UpdateUserRequestDto.builder()
                .name(newName)
                .build();
    }
}
