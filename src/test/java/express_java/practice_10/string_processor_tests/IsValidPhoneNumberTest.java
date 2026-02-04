package express_java.practice_10.string_processor_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IsValidPhoneNumberTest extends StringProcessorTest {

    @ParameterizedTest
    @CsvSource({
            "+1 1234567890, true",
            "+123 1234567890, true",
            "+123 123456789, false",
            "+1234 1234567890, false",
            "+123 asdfghjklo, false",
            "+tst 1234567890, false",
            "12345, false",
            "test, false",
            "'', false",
            "'  ', false",
    })
    public void userCanCheckPhoneNumberIsValid(String input, boolean expectedResult) {
        boolean actualResult = processor.isValidPhoneNumber(input);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void userCannotCheckPhoneNumberIsNull() {
        assertThrows(NullPointerException.class, () -> processor.isValidPhoneNumber(null));
    }
}
