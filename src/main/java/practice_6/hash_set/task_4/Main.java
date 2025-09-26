package main.java.practice_6.hash_set.task_4;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Создайте HashSet, содержащий набор имен. Напишите программу, которая проверяет, содержится ли ваше имя в множестве, и выводит соответствующее сообщение.
 */

public class Main {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();

        set.add("Миша");
        set.add("Аня");
        set.add("Ваня");
        set.add("Егор");
        set.add("Даша");

        checkName(set);
    }
    public static void checkName(Set<String> set) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();

        if (!set.isEmpty()) {
            if (set.contains(name)) {
                System.out.println("Имя уже есть в множестве");
            } else {
                System.out.println("Имя отсутствует в множестве");
            }
        } else {
            System.out.println("Множество пустое");
        }

        sc.close();
    }
}
