package express_java.practice_12.task_6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static express_java.practice_12.task_6.Priority.HIGH;
import static express_java.practice_12.task_6.Status.IN_PROGRESS;
import static express_java.practice_12.task_6.Status.OPEN;

public class GetTasksByStatusTests extends TaskServiceTest {

    @Test
    public void getTasksByStatusTest() {
        Task<Integer> task1 = new Task<>(1, OPEN, HIGH);
        taskService.addTask(task1);
        Task<Integer> task2 = new Task<>(2, IN_PROGRESS, HIGH);
        taskService.addTask(task2);
        Task<Integer> task3 = new Task<>(3, OPEN, HIGH);
        taskService.addTask(task3);

        List<Task<Integer>> actualTasks = taskService.getTasksByStatus(OPEN);

        Assertions.assertEquals(2, actualTasks.size());
        Assertions.assertTrue(actualTasks.contains(task1));
        Assertions.assertTrue(actualTasks.contains(task3));
        Assertions.assertFalse(actualTasks.contains(task2));
    }

    @Test
    public void getTasksByInvalidStatusTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskService.getTasksByStatus(null));
    }

    @Test
    public void getTasksByStatusFromEmptyMapTest() {
        List<Task<Integer>> actualTasks = taskService.getTasksByStatus(OPEN);

        Assertions.assertTrue(actualTasks.isEmpty());
    }
}
