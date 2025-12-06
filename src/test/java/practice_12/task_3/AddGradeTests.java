package practice_12.task_3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice_12.task_3.exceptions.InvalidGradeException;

import java.util.stream.Stream;

public class AddGradeTests extends GradeServiceTest {

    @Test
    public void AddGradeTest() {
        StudentGrade<Double> grade = new StudentGrade<>("Иван", "Математика", 4.0);
        gradeService.addGrade(grade);

        Assertions.assertEquals(1, gradeService.size());
        Assertions.assertTrue(gradeService.contains(grade));
    }

    @ParameterizedTest
    @MethodSource("invalidGradeProvider")
    public void AddInvalidGradeTest(StudentGrade<Double> grade) {
        Assertions.assertThrows(InvalidGradeException.class, () -> gradeService.addGrade(grade));
    }

    private static Stream<Arguments> invalidGradeProvider() {
        return Stream.of(
                Arguments.of(new StudentGrade<>("Иван", "Математика", -0.1)),
                Arguments.of(new StudentGrade<>(null, "Математика", 4.0)),
                Arguments.of(new StudentGrade<>("Иван", null, 4.0)),
                Arguments.of(new StudentGrade<>("Иван", "Математика", null)),
                Arguments.of((Object) null)
        );
    }

    @Test
    public void AddStudentGradeWithMultithreadingTest() {
        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                gradeService.addGrade(new StudentGrade<>("Иван", "Математика", 4.0));
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(200, gradeService.size());
    }
}
