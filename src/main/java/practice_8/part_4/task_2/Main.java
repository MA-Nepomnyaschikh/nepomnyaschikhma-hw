package practice_8.part_4.task_2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Напишите программу, которая принимает список чисел и группирует их на чётные и нечётные, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(groupByParity(list));
    }

    public static Map<Boolean, List<Integer>> groupByParity(List<Integer> list) {
        return list.stream()
                .collect(Collectors.groupingBy(x -> x % 2 == 0));
    }
}
