package express_java.practice_12.task_6;

import java.time.LocalDate;
import java.util.Objects;

public class Task <T> {
    private final T id;
    private Status status;
    private Priority priority;
    private final LocalDate date;

    public Task(T id, Status status, Priority priority, LocalDate date) {
        this.id = id;
        this.status = status;
        this.priority = priority;
        this.date = date;
    }

    public Task(T id, Status status, Priority priority) {
        this.id = id;
        this.status = status;
        this.priority = priority;
        this.date = LocalDate.now();
    }

    public T getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task<?> task = (Task<?>) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
