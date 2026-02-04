package express_java.practice_12.task_6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static express_java.practice_12.task_6.Priority.*;
import static express_java.practice_12.task_6.Status.*;

public class RemoveTaskTests extends TaskServiceTest {

    @Test
    public void removeTaskTest() {
        Task<Integer> task = new Task<>(1, OPEN, HIGH);
        taskService.addTask(task);

        Task<Integer> removedTask = taskService.removeTask(task.getId());

        Assertions.assertEquals(0, taskService.size());
        Assertions.assertEquals(task, removedTask);
    }

    @ParameterizedTest
    @MethodSource("invalidTaskProvider")
    public void removeInvalidTaskTest(Task<Integer> task) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskService.removeTask(task.getId()));
    }

    private static Stream<Arguments> invalidTaskProvider() {
        return Stream.of(
                Arguments.of(new Task<Integer>(null, OPEN, HIGH)),
                Arguments.of(new Task<Integer>(1, OPEN, HIGH))
        );
    }

    @Test
    public void removeDuplicateTaskTest() {
        Task<Integer> task = new Task<>(1, OPEN, HIGH);
        taskService.addTask(task);

        taskService.removeTask(task.getId());

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskService.removeTask(task.getId()));
    }
}
