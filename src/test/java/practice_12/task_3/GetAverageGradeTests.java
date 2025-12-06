package practice_12.task_3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice_12.task_3.exceptions.InvalidGradeException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GetAverageGradeTests extends GradeServiceTest {

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void getAverageGradeTest(String subject, double expectedAverage) {
        List<StudentGrade<Double>> studentGrades = new ArrayList<>(List.of(
                new StudentGrade<>("Иван", "Математика", 5.0),
                new StudentGrade<>("Иван", "Русский язык", 2.0),
                new StudentGrade<>("Иван", "Математика", 3.0),
                new StudentGrade<>("Иван", "Русский язык", 4.0),
                new StudentGrade<>("Иван", "Информатика", 3.0)
        ));
        studentGrades.forEach(gradeService::addGrade);

        double actualAverage = gradeService.getAverageGrade(subject);

        Assertions.assertEquals(expectedAverage, actualAverage, 0.001);
    }

    private static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of("Математика", 4.0),
                Arguments.of("Русский язык", 3.0),
                Arguments.of("История", 0.0)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    public void getAverageGradeWithInvalidSubjectTest(String subject) {
        Assertions.assertThrows(InvalidGradeException.class, () -> gradeService.getAverageGrade(subject));
    }

    private static Stream<Arguments> invalidDataProvider() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("   "),
                Arguments.of((Object) null)
        );
    }

}
