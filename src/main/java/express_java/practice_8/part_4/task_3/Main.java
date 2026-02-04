package express_java.practice_8.part_4.task_3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Напишите программу, которая принимает список чисел и находит их среднее значение, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(calculateAverage(list));
    }

    public static double calculateAverage(List<Integer> list) {
        return list.stream()
                .collect(Collectors.averagingInt(Integer::intValue));
    }
}
