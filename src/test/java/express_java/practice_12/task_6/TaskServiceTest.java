package express_java.practice_12.task_6;

import express_java.practice_12.task_6.TaskService;
import org.junit.jupiter.api.BeforeEach;

public class TaskServiceTest {

    protected TaskService<Integer> taskService;

    @BeforeEach
    public void setUp() {
        taskService = new TaskService<>();
    }
}
