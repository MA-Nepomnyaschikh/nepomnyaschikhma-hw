package main.java.practice_5.task_2;

public class Cat extends Pet {

    @Override
    public void interact() {
        System.out.println("Кошка играет");
    }

    @Override
    public void eat() {
        System.out.println("Кошка ест влажный корм");
    }
}
