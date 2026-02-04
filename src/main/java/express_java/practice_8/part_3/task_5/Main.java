package express_java.practice_8.part_3.task_5;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Напишите программу, которая проверяет, есть ли хотя бы один элемент в списке,
 * который удовлетворяет заданному условию (например, является чётным числом), используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(hasMatchingElement(list1, x -> x % 2 == 0));

        List<String> list2 = new ArrayList<>(List.of("adsfg", "bsd2", "cfdhkss", "fghh", "esdfsfdff", "ff2g", "g", "fh"));
        System.out.println(hasMatchingElement(list2, s -> s.length() == 3));
    }

    public static <T> boolean hasMatchingElement(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .anyMatch(predicate);
    }
}
