package main.java.practice_8.part_2.task_2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Напишите программу, которая принимает список чисел и отбирает только те, которые делятся на 5 без остатка, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(filterMultiplesOf(5, list));
    }

    private static List<Integer> filterMultiplesOf(int number, List<Integer> list) {
        return list.stream()
                .filter(x -> x % number == 0)
                .collect(Collectors.toList());
    }
}
