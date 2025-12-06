package practice_12.task_6.validators;

import practice_12.task_6.Priority;
import practice_12.task_6.Status;
import practice_12.task_6.Task;

public class TaskValidator {
    public static void validateTask(Task<?> task) {
        if (task == null) throw new IllegalArgumentException("Task cannot be null");
        validateId(task.getId());
        validateStatus(task.getStatus());
        validatePriority(task.getPriority());
    }

    public static void validateStatus(Status status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null");
    }

    public static void validatePriority(Priority priority) {
        if (priority == null) throw new IllegalArgumentException("Priority cannot be null");
    }

    public static <T> void validateId(T id) {
        if (id == null) throw new IllegalArgumentException("Id cannot be null");
    }
}
