package main.java.practice_5.task_3;

public class HotDish extends Dish {

    private double temperature;

    public HotDish(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public void printInfo() {
        System.out.printf("Температура горячего блюда: " + temperature + " градусов");
    }
}
