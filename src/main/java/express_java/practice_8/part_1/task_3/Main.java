package express_java.practice_8.part_1.task_3;

import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        Predicate<Integer> isEven = i -> i % 2 == 0;

        System.out.println(isEven.test(2));
        System.out.println(isEven.test(3));
    }
}
