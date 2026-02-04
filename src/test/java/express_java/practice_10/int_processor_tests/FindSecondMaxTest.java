package express_java.practice_10.int_processor_tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FindSecondMaxTest extends IntProcessorTest {

    @ParameterizedTest
    @MethodSource("getArray")
    public void userCanFindSecondMaxElementFromValidArray(int[] array, int expectedMaxElement) {
        int actualMaxElement = processor.findSecondMax(array);
        assertEquals(expectedMaxElement, actualMaxElement);
    }

    public static Stream<Arguments> getArray() {
        return Stream.of(
                Arguments.of(new int[]{1, 2, 3}, 2),
                Arguments.of(new int[]{-10, -5, 0}, -5)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidArraysProvider")
    void userCannotFindSecondMaxFromInvalidArray(int[] input, Class<? extends Throwable> expectedException) {
        assertThrows(expectedException, () -> processor.findSecondMax(input));
    }

    static Stream<Arguments> invalidArraysProvider() {
        return Stream.of(
                Arguments.of(new int[]{5, 5, 5}, NoSuchElementException.class),
                Arguments.of(new int[]{3}, IllegalArgumentException.class),
                Arguments.of(new int[]{}, IllegalArgumentException.class),
                Arguments.of(null, NullPointerException.class)
        );
    }
}
