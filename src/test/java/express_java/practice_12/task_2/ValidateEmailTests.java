package express_java.practice_12.task_2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import express_java.practice_12.task_2.exceptions.InvalidUserException;

public class ValidateEmailTests extends UserValidatorTest {


    @ParameterizedTest
    @ValueSource(strings = {"te.st@mail.ru", "test-+_test%@yandex.com"})
    public void validateEmailTest(String email) {
        User user = new User("Ivan", 18, email);
        Assertions.assertDoesNotThrow(() ->validator.validateEmail(user.getEmail()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"te@st@mail.ru", "test+-_test%yandex.com", "test+-_test%@yandex.testestest", "@stmail.ru", ""})
    public void validateInvalidEmailTest(String email) {
        User user = new User("Ivan", 18, email);
        Assertions.assertThrows(InvalidUserException.class, () -> validator.validateEmail(user.getEmail()));
    }

    @Test
    public void validateNullEmailTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> validator.validateEmail(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"te@st@mail.ru", "test+-_test%yandex.com", "test+-_test%@yandex.testestest", "@stmail.ru", ""})
    public void validateEmailWithDisableFlagTest(String email) {
        UserValidator.setFlag(false);
        User user = new User("Ivan", 18, email);
        Assertions.assertDoesNotThrow(() ->validator.validateEmail(user.getEmail()));
    }

}
