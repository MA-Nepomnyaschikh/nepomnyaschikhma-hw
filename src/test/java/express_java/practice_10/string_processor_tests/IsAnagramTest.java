package express_java.practice_10.string_processor_tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsAnagramTest extends StringProcessorTest {

    @ParameterizedTest
    @CsvSource(value = {
            "test, tset, true",
            "aba, aba, true",
            "'', '', true",
            "java, python, false",
            "null, llun, false",
            "llun, null, false"
    },
            nullValues = {"null"}
    )
    public void userCanCheckStringIsAnagram(String str1, String str2, boolean expectedResult) {
        boolean actualResult = processor.isAnagram(str1, str2);
        assertEquals(expectedResult, actualResult);
    }
}
