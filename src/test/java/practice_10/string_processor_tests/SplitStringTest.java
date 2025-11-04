package practice_10.string_processor_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SplitStringTest extends StringProcessorTest {

    @ParameterizedTest
    @MethodSource("getList")
    public void userCanSplitStringIfStringIsValid(String input, String delimiter, List<String> expectedList) {
        List<String> actualList = List.of(processor.splitString(input, delimiter));
        assertEquals(expectedList, actualList);
    }

    public static Stream<Arguments> getList() {
        return Stream.of(
                Arguments.of("Java,Python,C++", ",", List.of("Java", "Python", "C++")),
                Arguments.of("word", ",", List.of("word")),
                Arguments.of("", ",", List.of(""))
        );
    }

    @Test
    void userCannotSplitStringIfStringIsNull() {
        assertThrows(NullPointerException.class, () -> processor.splitString(null, null));
    }
}
