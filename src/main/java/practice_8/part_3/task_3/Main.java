package practice_8.part_3.task_3;

import java.util.ArrayList;
import java.util.List;

/**
 * Напишите программу, которая принимает список чисел и вычисляет их сумму, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(sum(list));
    }

    public static int sum(List<Integer> list) {
        return list.stream()
                .mapToInt(x -> x)
                .sum();
    }
}
