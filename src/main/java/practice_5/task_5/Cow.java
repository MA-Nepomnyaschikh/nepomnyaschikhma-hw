package main.java.practice_5.task_5;

public class Cow extends Animal {
    @Override
    public void eat() {
        System.out.println("Корова пасется");
    }

    @Override
    public void produce() {
        System.out.println("Корова дает молоко");
    }
}
