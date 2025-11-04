package main.java.practice_6.linked_list.task_3;

import java.util.LinkedList;
import java.util.List;

/**
 * Создайте LinkedList, содержащий несколько строк. Напишите программу, которая печатает первый и последний элементы списка.
 */

public class Main {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>(List.of("test1", "test2","test3","test4","test5","test6"));
        printFirstAndLast(list);
    }

    public static void printFirstAndLast(LinkedList<String> list) {
        System.out.println(list.getFirst());
        System.out.println(list.getLast());
    }
}
