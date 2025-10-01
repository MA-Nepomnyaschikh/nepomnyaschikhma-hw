package main.java.practice_7.optional_tasks.task_6;

public class Box <T> implements Container<T> {

    private T item;

    @Override
    public T getItem() {
        return this.item;
    }

    public void add(T item) {
        this.item = item;
    }
}
