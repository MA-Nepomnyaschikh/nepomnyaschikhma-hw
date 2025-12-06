package practice_12.task_6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static practice_12.task_6.Priority.HIGH;
import static practice_12.task_6.Priority.LOW;
import static practice_12.task_6.Status.OPEN;

public class GetTasksByPriorityTests extends TaskServiceTest {

    @Test
    public void getTasksByPriorityTest() {
        Task<Integer> task1 = new Task<>(1, OPEN, HIGH);
        taskService.addTask(task1);
        Task<Integer> task2 = new Task<>(2, OPEN, LOW);
        taskService.addTask(task2);
        Task<Integer> task3 = new Task<>(3, OPEN, HIGH);
        taskService.addTask(task3);

        List<Task<Integer>> actualTasks = taskService.getTasksByPriority(HIGH);

        Assertions.assertEquals(2, actualTasks.size());
        Assertions.assertTrue(actualTasks.contains(task1));
        Assertions.assertTrue(actualTasks.contains(task3));
        Assertions.assertFalse(actualTasks.contains(task2));
    }

    @Test
    public void getTasksByInvalidPriorityTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskService.getTasksByPriority(null));
    }

    @Test
    public void getTasksByPriorityFromEmptyMapTest() {
        List<Task<Integer>> actualTasks = taskService.getTasksByPriority(HIGH);

        Assertions.assertTrue(actualTasks.isEmpty());
    }
}
