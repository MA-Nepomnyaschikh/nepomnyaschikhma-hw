package practice_12.task_2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import practice_12.task_2.exceptions.InvalidUserException;

public class ValidateAgeTests extends UserValidatorTest {

    @ParameterizedTest
    @ValueSource(ints = {18, 50, 100})
    public void validateAgeTest(int age) {
        User user = new User("Ivan", age, "test@mail.ru");
        Assertions.assertDoesNotThrow(() ->validator.validateAge(user.getAge()));
    }

    @ParameterizedTest
    @ValueSource(ints = {17, 101})
    public void validateInvalidAgeTest(int age) {
        User user = new User("Ivan", age, "test@mail.ru");
        Assertions.assertThrows(InvalidUserException.class, () ->validator.validateAge(user.getAge()));
    }

    @ParameterizedTest
    @ValueSource(ints = {17, 101})
    public void validateAgeWithDisableFlagTest(int age) {
        UserValidator.setFlag(false);
        User user = new User("Ivan", age, "test@mail.ru");
        Assertions.assertDoesNotThrow(() ->validator.validateAge(user.getAge()));
    }

}
