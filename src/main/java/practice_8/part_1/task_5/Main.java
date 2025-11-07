package practice_8.part_1.task_5;

import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        Consumer<String> print = System.out::println;
        print.accept("Hello");
        print.accept("Hello!!!");
    }
}
