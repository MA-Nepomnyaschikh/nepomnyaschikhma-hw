package practice_10.int_processor_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class IsEvenTest extends IntProcessorTest {

    @ParameterizedTest
    @ValueSource(ints = { 4, 0, -2})
    public void userCanCheckIfValidIntIsEven(int input) {
        boolean result = processor.isEven(input);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(ints = {5})
    public void userCannotCheckIfInvalidIntIsEven(int input) {
        boolean result = processor.isEven(input);
        Assertions.assertFalse(result);
    }
}
