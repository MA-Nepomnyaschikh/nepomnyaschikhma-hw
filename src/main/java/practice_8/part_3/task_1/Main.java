package practice_8.part_3.task_1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Напишите программу, которая принимает список чисел и находит в нём самое большое число, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        List<Integer> list2 = new ArrayList<>();
        System.out.println(findMax(list1));
        System.out.println(findMax(list2));
    }

    public static int findMax(List<Integer> list) {
        return list.stream()
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
