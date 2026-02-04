package express_java.practice_7.generics_tasks.task_1;

public class Box <T> {

    private T element;

    public Box(T element) {
        this.element = element;
    }

    public Box() {
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }
}
