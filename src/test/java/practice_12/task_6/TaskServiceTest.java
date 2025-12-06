package practice_12.task_6;

import org.junit.jupiter.api.BeforeEach;

public class TaskServiceTest {

    protected TaskService<Integer> taskService;

    @BeforeEach
    public void setUp() {
        taskService = new TaskService<>();
    }
}
