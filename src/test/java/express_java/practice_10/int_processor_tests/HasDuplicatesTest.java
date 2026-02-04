package express_java.practice_10.int_processor_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HasDuplicatesTest extends IntProcessorTest {

    @ParameterizedTest
    @MethodSource("getArray")
    public void userCanFilterIfListIsValid(int [] array, boolean expectedResult) {
        boolean actualResult = processor.hasDuplicates(array);
        assertEquals(expectedResult, actualResult);
    }

    public static Stream<Arguments> getArray() {
        return Stream.of(
                Arguments.of(new int [] {1, 2, 3, 4, 5}, false),
                Arguments.of(new int [] {1, 2, 2, 4, 3}, true),
                Arguments.of(new int [] {}, false)
        );
    }

    @Test
    void userCannotFilterIfListIsNull() {
        assertThrows(NullPointerException.class, () -> processor.hasDuplicates(null));
    }
}
