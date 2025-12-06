package practice_12.task_6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static practice_12.task_6.Priority.HIGH;
import static practice_12.task_6.Status.OPEN;

public class SortTasksByDateTests extends TaskServiceTest {

    @Test
    public void sortTasksByDateTest() {
        Task<Integer> t1 = new Task<>(1, OPEN, HIGH, LocalDate.of(2023, 02, 15));
        Task<Integer> t2 = new Task<>(2, OPEN, HIGH, LocalDate.of(2023, 02, 12));
        Task<Integer> t3 = new Task<>(3, OPEN, HIGH, LocalDate.of(2023, 01, 12));
        Task<Integer> t4 = new Task<>(4, OPEN, HIGH, LocalDate.of(2020, 02, 15));
        Task<Integer> t5 = new Task<>(5, OPEN, HIGH, LocalDate.of(2020, 02, 12));
        Task<Integer> t6 = new Task<>(6, OPEN, HIGH, LocalDate.of(2020, 01, 12));

        List<Task<Integer>> tasks = List.of(t1, t2, t3, t4, t5, t6);
        tasks.forEach(taskService::addTask);

        List<Task<Integer>> expectedTasks = List.of(t1, t2, t3, t4, t5, t6);

        List<Task<Integer>> actualTasks = taskService.sortTasksByDate();

        Assertions.assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void sortTasksByDateFromEmptyMapTest() {
        List<Task<Integer>> actualTasks = taskService.sortTasksByDate();

        Assertions.assertTrue(actualTasks.isEmpty());
    }
}
