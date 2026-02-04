package express_java.practice_8.part_1.task_4;

import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<String, Integer> str = s -> s.length();

        System.out.println(str.apply("hello"));
        System.out.println(str.apply("hello!!!"));
    }
}
