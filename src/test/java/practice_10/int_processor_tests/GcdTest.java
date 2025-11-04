package practice_10.int_processor_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GcdTest extends IntProcessorTest {

    @ParameterizedTest
    @CsvSource({
            "24, 36, 12",
            "101, 103, 1",
            "101, -103, 1",
            "0, 10, 10",
            "10, 0, 10"
    })
    public void userCanGetGcdIfNumberIsValid(int a, int b, int expectedRusult) {
        int actualResult = processor.gcd(a, b);
        Assertions.assertEquals(expectedRusult, actualResult);
    }
}
