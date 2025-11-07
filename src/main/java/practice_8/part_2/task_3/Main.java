package practice_8.part_2.task_3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Напишите программу, которая принимает список строк и заменяет каждую строку на её длину, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("adsfg", "bsd", "cfdhkss", "fghh", "esdfsfdff", "ffg", "g", "fh"));
        System.out.println(getStringLengths(list));
    }

    private static List<Integer> getStringLengths(List<String> list) {
        return list.stream()
                .map(String::length)
                .collect(Collectors.toList());
    }
}
