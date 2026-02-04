package express_java.practice_12.task_3;

import express_java.practice_12.task_3.GradeService;
import org.junit.jupiter.api.BeforeEach;

public class GradeServiceTest {

    protected GradeService<Double> gradeService;

    @BeforeEach
    public void setUp() {
        gradeService = new GradeService<>();
    }
}
