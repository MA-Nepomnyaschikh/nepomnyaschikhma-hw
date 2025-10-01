package main.java.practice_7.optional_tasks.task_6;

public class Main {
    public static void main(String[] args) {
        Container<String> cardholder_1 = new Box<>();
        Container<Integer> cardholder_2 = new Box<>();

        cardholder_1.add("Card 1");
        cardholder_2.add(10);

        System.out.println(cardholder_1.getItem());
        System.out.println(cardholder_2.getItem());
    }
}
