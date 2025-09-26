package main.java.practice_6.array_list.task_5;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Создайте ArrayList из целых чисел. Напишите программу, которая находит и выводит максимальное число из списка.
 */

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            list.add(i);
        }

        System.out.println(Collections.max(list));
        getMax(list);
    }

    public static void getMax(ArrayList<Integer> list) {
        if (!list.isEmpty()) {
            int max = Integer.MIN_VALUE;
            for (Integer i : list) {
                if (i > max) {
                    max = i;
                }
            }
            System.out.println(max);
        } else {
            System.out.println("List is empty");
        }
    }
}
