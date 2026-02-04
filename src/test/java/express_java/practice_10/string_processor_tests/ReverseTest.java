package express_java.practice_10.string_processor_tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReverseTest extends StringProcessorTest {

    @ParameterizedTest
    @CsvSource(value = {
            "hello, olleh",
            "12345, 54321",
            "a, a",
            "Test, tseT",
            "'', ''",
            "null, null"
    },
            nullValues = {"null"}
    )
    public void userCanGetReverseStringIfStringIsValid(String input, String expectedResult) {
        String actualResult = processor.reverse(input);
        assertEquals(expectedResult, actualResult);
    }
}
