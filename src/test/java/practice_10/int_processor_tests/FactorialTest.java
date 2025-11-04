package practice_10.int_processor_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FactorialTest extends IntProcessorTest {

    @ParameterizedTest
    @CsvSource({
            "5, 120",
            "1, 1",
            "0 , 1"
    })
    public void userCanGetFactorialIfNumberIsValid(int number, int expectedRusult) {
        int actualResult = processor.factorial(number);
        Assertions.assertEquals(expectedRusult, actualResult);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1 , -10})
    public void userCannotGetFactorialIfNumberIsInvalid(int number) {
        assertThrows(IllegalArgumentException.class, () -> processor.factorial(number));
    }
}
