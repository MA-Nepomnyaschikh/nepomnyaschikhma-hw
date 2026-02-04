package express_java.practice_8.part_2.task_4;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Напишите программу, которая принимает список чисел и преобразует его в новый список, где каждое число заменено на его квадрат, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(getSquaresList(list));
    }

    private static List<Integer> getSquaresList(List<Integer> list) {
        return list.stream()
                .map(x -> x * x)
                .collect(Collectors.toList());
    }
}
