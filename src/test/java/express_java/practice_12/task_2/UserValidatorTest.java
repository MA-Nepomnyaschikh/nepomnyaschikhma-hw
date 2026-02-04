package express_java.practice_12.task_2;

import express_java.practice_12.task_2.UserValidator;
import org.junit.jupiter.api.BeforeEach;

public class UserValidatorTest {

    protected UserValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new UserValidator();
        UserValidator.setFlag(true);
    }
}
