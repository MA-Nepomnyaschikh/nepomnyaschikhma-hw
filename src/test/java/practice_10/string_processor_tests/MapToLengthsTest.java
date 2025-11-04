package practice_10.string_processor_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapToLengthsTest extends StringProcessorTest {

    @ParameterizedTest
    @MethodSource("getList")
    public void userCanGetListLengthIfListIsValid(List<String> list, List<Integer> expectedList) {
        List<Integer> actualList = processor.mapToLengths(list);
        assertEquals(expectedList, actualList);
    }

    public static Stream<Arguments> getList() {
        return Stream.of(
                Arguments.of(List.of("Java", "C++", "Go" ), List.of(4, 3 ,2)),
                Arguments.of(List.of("", " ", "  " ), List.of(0, 1 ,2)),
                Arguments.of(List.of(), List.of())
        );
    }

    @Test
    void userCannotGetListLengthIfListIsNull() {
        assertThrows(NullPointerException.class, () -> processor.mapToLengths(null));
    }
}
