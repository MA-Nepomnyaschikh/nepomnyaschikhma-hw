package practice_10.string_processor_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SortByLengthTest extends StringProcessorTest {

    @ParameterizedTest
    @MethodSource("getList")
    public void userCanFilterIfListIsValid(List<String> list, List<String> expectedList) {
        List<String> actualList = processor.sortByLength(list);
        assertEquals(expectedList, actualList);
    }

    public static Stream<Arguments> getList() {
        return Stream.of(
                Arguments.of(List.of("Java", "C", "Python"), List.of("C", "Java", "Python")),
                Arguments.of(List.of("aa", "bb", "cc"), List.of("aa", "bb", "cc")),
                Arguments.of(List.of(), List.of())
        );
    }

    @Test
    void userCannotFilterIfListIsNull() {
        assertThrows(NullPointerException.class, () -> processor.sortByLength(null));
    }
}
