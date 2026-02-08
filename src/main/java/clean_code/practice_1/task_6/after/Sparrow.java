package clean_code.practice_1.task_6.after;

public class Sparrow extends Bird implements Flyable {
    @Override
    void eat() {
        System.out.println("Воробой ест");
    }

    @Override
    public void fly() {
        System.out.println("Воробей летит");
    }
}
