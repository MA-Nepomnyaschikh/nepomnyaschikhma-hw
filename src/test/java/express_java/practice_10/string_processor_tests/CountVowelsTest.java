package express_java.practice_10.string_processor_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountVowelsTest extends StringProcessorTest {

    @ParameterizedTest
    @CsvSource({
            "hello, 2",
            "AEIOU, 5",
            "FGHTKX, 0",
            "'', 0"
    })
    public void userCanGetCountVowelsFromValidString(String input, int expectedCount) {
        int actualCount = processor.countVowels(input);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void userCannotGetCountVowelsFromNullString() {
        assertThrows(IllegalArgumentException.class, () -> processor.countVowels(null));
    }
}
