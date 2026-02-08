package clean_code.practice_1.task_7.after;

public class Director implements Workable, Eatable {

    @Override
    public void eat() {
        System.out.println("Начальник ест");
    }

    @Override
    public void work() {
        System.out.println("Начальник спрашивает у программиста, когда фича поедет в прод");
    }
}
