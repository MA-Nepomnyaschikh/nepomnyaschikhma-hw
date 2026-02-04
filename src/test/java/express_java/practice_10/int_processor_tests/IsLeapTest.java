package express_java.practice_10.int_processor_tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class IsLeapTest extends IntProcessorTest {

    @ParameterizedTest
    @CsvSource({
            "2024, true",
            "2025, false",
            "1900, false",
            "0, true",
            "4, true",
            "400, true",
    })
    public void userCanCheckIfValidYearIsLeap(int year, boolean expectedResult) {
        boolean actualResult = processor.isLeapYear(year);
        assertEquals(expectedResult, actualResult);
    }
}
