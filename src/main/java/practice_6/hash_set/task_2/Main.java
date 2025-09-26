package main.java.practice_6.hash_set.task_2;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        set.add(6);
        set.add(7);
        set.add(8);
        set.add(9);
        set.add(10);

        containsNumber(set, 11);
    }

    public static void containsNumber(Set<Integer> set, int number) {
        boolean result = set.contains(number);
        if (result) {
            System.out.println("Число " + number + " содержится в множестве");
        } else {
            System.out.println("Число " + number + " отсутствует в множестве");
        }
    }
}
