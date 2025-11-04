package practice_10.int_processor_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilterEvenNumbersTest extends IntProcessorTest {

    @ParameterizedTest
    @MethodSource("getList")
    public void userCanFilterIfListIsValid(List<Integer> list, List<Integer> expectedList) {
        List<Integer> actualList = processor.filterEvenNumbers(list);
        assertEquals(expectedList, actualList);
    }

    public static Stream<Arguments> getList() {
        return Stream.of(
                Arguments.of(List.of(1, 2, 3, 4, 5), List.of(2, 4)),
                Arguments.of(List.of(1, 3, 5), List.of()),
                Arguments.of(List.of(), List.of())
        );
    }

    @Test
    void userCannotFilterIfListIsNull() {
        assertThrows(NullPointerException.class, () -> processor.filterEvenNumbers(null));
    }
}
