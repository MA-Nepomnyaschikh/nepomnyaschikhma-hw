package main.java.practice_6.hash_set.task_3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Реализуйте метод, который принимает List<String> и возвращает Set<String> без дубликатов.
 */

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("a", "c", "a", "b", "c", "d", "a", "e", "c", "f", "g", "h"));
        System.out.println(getSetFromList(list));
    }

    public static Set<String> getSetFromList(List<String> list) {
        return new HashSet<>(list);
    }
}
