package express_java.practice_10.string_processor_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountWordsTest extends StringProcessorTest {

    @ParameterizedTest
    @CsvSource({
            "hello test, 2",
            "hello test   , 2",
            "'  ' , 0",
            "AEIOU, 1",
            "'', 0"
    })
    public void userCanGetCountWordsFromValidString(String input, int expectedCount) {
        int actualCount = processor.countWords(input);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void userCannotGetCountWordsFromNullString() {
        assertThrows(NullPointerException.class, () -> processor.countWords(null));
    }
}
