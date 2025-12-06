package practice_12.task_6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static practice_12.task_6.Priority.*;
import static practice_12.task_6.Status.*;

public class AddTaskTests extends TaskServiceTest {

    @Test
    public void addTaskTest() {
        Task<Integer> task = new Task<>(1, OPEN, HIGH);
        taskService.addTask(task);

        Assertions.assertEquals(1, taskService.size());
        Assertions.assertEquals(task, taskService.getTasks().getFirst());
    }

    @ParameterizedTest
    @MethodSource("invalidTaskProvider")
    public void addInvalidTaskTest(Task<Integer> task) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskService.addTask(task));
    }

    private static Stream<Arguments> invalidTaskProvider() {
        return Stream.of(
                Arguments.of(new Task<Integer>(null, OPEN, HIGH)),
                Arguments.of(new Task<Integer>(1, OPEN, null)),
                Arguments.of(new Task<Integer>(2, null, HIGH)),
                Arguments.of((Object) null)
        );
    }

    @Test
    public void addDuplicateTaskTest() {
        Task<Integer> task1 = new Task<>(1, OPEN, HIGH);
        Task<Integer> task2 = new Task<>(1, IN_PROGRESS, LOW);
        taskService.addTask(task1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskService.addTask(task2));
        Assertions.assertEquals(1, taskService.size());
    }
}
