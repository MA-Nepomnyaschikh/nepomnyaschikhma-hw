package express_java.practice_10.string_processor_tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsValidPasswordTest extends StringProcessorTest {

    @ParameterizedTest
    @CsvSource(value = {
            "Password1, true",
            "pass, false",
            "'', false",
            "null, false"
    },
            nullValues = {"null"}
    )
    public void userCanCheckPasswordIsValid(String input, boolean expectedResult) {
        boolean actualResult = processor.isValidPassword(input);
        assertEquals(expectedResult, actualResult);
    }
}
