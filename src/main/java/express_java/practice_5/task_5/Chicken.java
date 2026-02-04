package express_java.practice_5.task_5;

public class Chicken extends Animal {

    @Override
    public void eat() {
        System.out.println("Курица ест комбикорм");
    }

    @Override
    public void produce() {
        System.out.println("Курица несет яйца");
    }
}
