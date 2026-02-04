package express_java.practice_7.optional_tasks.task_1;

public class Storage <T> {
    private T element;

    public Storage(T element){
        this.element = element;
    }

    public Storage(){
    }

    public T getElement(){
        return element;
    }

    public void setElement(T element){
        this.element = element;
    }
}
