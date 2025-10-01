package main.java.practice_7.optional_tasks.task_2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> list1 = new ArrayList<>(List.of("a", "b", "c", "d", "e", "f", "g", "h"));
        LinkedList<Integer> list2 = new LinkedList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        printList(list1);
        System.out.println();
        printList(list2);

    }

    public static <T> void printList(List<T> list) {
        for (T element : list) {
            System.out.println(element);
        }
    }
}
