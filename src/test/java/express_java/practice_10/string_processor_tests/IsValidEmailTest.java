package express_java.practice_10.string_processor_tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsValidEmailTest extends StringProcessorTest {

    @ParameterizedTest
    @CsvSource(value = {
            "test@example.com, true",
            "no-at-symbol, false",
            "bad@.com, false",
            "'', false",
            "null, false"
    },
            nullValues = {"null"}
    )
    public void userCanCheckEmailIsValid(String input, boolean expectedResult) {
        boolean actualResult = processor.isValidEmail(input);
        assertEquals(expectedResult, actualResult);
    }
}
