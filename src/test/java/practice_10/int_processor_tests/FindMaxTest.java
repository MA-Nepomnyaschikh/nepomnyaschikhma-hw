package practice_10.int_processor_tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FindMaxTest extends IntProcessorTest {

    @ParameterizedTest
    @MethodSource("getArray")
    public void userCanFindMaxElementFromValidArray(int[] array, int expectedMaxElement) {
        int actualMaxElement = processor.findMax(array);
        assertEquals(expectedMaxElement, actualMaxElement);
    }

    public static Stream<Arguments> getArray() {
        return Stream.of(
                Arguments.of(new int[]{1, 2, 3}, 3),
                Arguments.of(new int[]{5, 5, 5}, 5),
                Arguments.of(new int[]{10, -5, 0}, 10),
                Arguments.of(new int[]{-10, -5, 0}, 0),
                Arguments.of(new int[]{3}, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidArraysProvider")
    void userCannotFindSecondMaxFromInvalidArray(int[] input, Class<? extends Throwable> expectedException) {
        assertThrows(expectedException, () -> processor.findMax(input));
    }

    static Stream<Arguments> invalidArraysProvider() {
        return Stream.of(
                Arguments.of(new int[]{}, NoSuchElementException.class),
                Arguments.of(null, NullPointerException.class)
        );
    }

}
