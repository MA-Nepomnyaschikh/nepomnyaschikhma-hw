package practice_12.task_3;

import org.junit.jupiter.api.BeforeEach;

public class GradeServiceTest {

    protected GradeService<Double> gradeService;

    @BeforeEach
    public void setUp() {
        gradeService = new GradeService<>();
    }
}
