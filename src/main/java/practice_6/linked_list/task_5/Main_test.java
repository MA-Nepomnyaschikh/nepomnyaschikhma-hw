package main.java.practice_6.linked_list.task_5;

import java.util.LinkedList;
import java.util.ListIterator;

public class Main_test {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.add("Задача 1");
        list.add("Задача 2");
        list.add("Задача 3");
        list.add("Задача 4");
        list.add("Задача 5");

        ListIterator<String> iterator = list.listIterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        while (iterator.hasPrevious()) {
            System.out.println(iterator.previous());
        }
    }
}
