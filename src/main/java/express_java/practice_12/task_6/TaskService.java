package express_java.practice_12.task_6;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static express_java.practice_12.task_6.validators.TaskValidator.*;

public class TaskService <T> {

    private final ConcurrentHashMap<T, Task<T>> taskService = new ConcurrentHashMap<>();

    public void addTask(Task<T> task) {
        validateTask(task);

        if (taskService.putIfAbsent(task.getId(), task) != null) {
            throw new IllegalArgumentException("Задача с указанным идентификатором уже присутствует в системе");
        }
    }

    public Task<T> removeTask(T id) {
        validateId(id);

        Task<T> task = taskService.remove(id);
        if (task == null) {
            throw new IllegalArgumentException("Задача с указанным идентификатором отсутствует в системе");
        }
        return task;
    }

    public Task<T> getTaskById(T id) {
        validateId(id);

        Task<T> task = taskService.get(id);
        if (task == null) {
            throw new IllegalArgumentException("Задача с указанным идентификатором отсутствует в системе");
        }
        return task;
    }

    public List<Task<T>> getTasks() {
        return new ArrayList<>(taskService.values());
    }

    public List<Task<T>> getTasksByStatus(Status status) {
        validateStatus(status);

        return taskService.values().stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Task<T>> getTasksByPriority(Priority priority) {
        validatePriority(priority);

        return taskService.values().stream()
                .filter(task -> task.getPriority().equals(priority))
                .collect(Collectors.toList());
    }

    public List<Task<T>> sortTasksByDate() {
        return taskService.values().stream()
                .sorted(Comparator.comparing((Task<T> t) -> t.getDate()).reversed())
                .collect(Collectors.toList());
    }

    public List<Task<T>> sortTasksByPriority() {
        return taskService.values().stream()
                .sorted(Comparator.comparing((Task<T> t) -> t.getPriority()).reversed())
                .collect(Collectors.toList());
    }

    public int size() {
        return taskService.size();
    }

    public boolean isEmpty() {
        return taskService.isEmpty();
    }


}
