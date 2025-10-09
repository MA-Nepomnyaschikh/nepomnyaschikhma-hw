package main.java.practice_8.part_2.task_5;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Напишите программу, которая принимает список элементов и удаляет из него все дубликаты, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, "Item_1"));
        items.add(new Item(2, "Item_2"));
        items.add(new Item(1, "Item_1"));
        items.add(new Item(3, "Item_3"));
        items.add(new Item(2, "Item_2"));
        items.add(new Item(2, "Item_2"));
        items.add(new Item(4, "Item_4"));

        System.out.println(removeDuplicates(items));

        List<Integer> list = new ArrayList<>(List.of(1, 1, 3, 4, 3, 6, 5, 8, 5, 6));

        System.out.println(removeDuplicates(list));
    }

    public static <T> List<T> removeDuplicates(List<T> items) {
        return items.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
