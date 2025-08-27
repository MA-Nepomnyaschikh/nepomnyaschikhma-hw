package main.java.practice_4.for_tasks;

/**
 * Задача:
 * Напишите программу, которая выводит числа от 1 до 10 на экран.
 * Методы и конструкции:
 * for
 * System.out.println()
 */

public class Task5 {
    public static void main(String[] args) {
        printNumbers();
    }

    private static void printNumbers() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(i);
        }
    }
}
