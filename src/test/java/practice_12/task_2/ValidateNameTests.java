package practice_12.task_2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import practice_12.task_2.exceptions.InvalidUserException;

public class ValidateNameTests extends UserValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"Ivan", "Andey", "M"})
    public void validateNameTest(String name) {
        User user = new User(name, 18, "test@mail.ru");
        Assertions.assertDoesNotThrow(() ->validator.validateName(user.getName()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "ivan"})
    public void validateInvalidNameTest(String name) {
        User user = new User(name, 18, "test@mail.ru");
        Assertions.assertThrows(InvalidUserException.class, () -> validator.validateName(user.getName()));
    }

    @Test
    public void validateNullNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> validator.validateName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "ivan"})
    public void validateNameWithDisableFlagTest(String name) {
        UserValidator.setFlag(false);
        User user = new User(name, 18, "test@mail.ru");
        Assertions.assertDoesNotThrow(() ->validator.validateName(user.getName()));
    }

}
