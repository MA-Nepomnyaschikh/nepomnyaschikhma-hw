package practice_10.string_processor_tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsValidJsonTest extends StringProcessorTest {

    @ParameterizedTest
    @MethodSource("getData")
    public void userCanFilterIfListIsValid(String input, boolean expectedResult) {
        boolean actualResult = processor.isValidJson(input);
        assertEquals(expectedResult, actualResult);
    }

    public static Stream<Arguments> getData() {
        return Stream.of(
                Arguments.of("{\"key\":\"value\"}", true),
                Arguments.of("", true),
                Arguments.of("invalid json", false),
                Arguments.of(null, false)
        );
    }
}
