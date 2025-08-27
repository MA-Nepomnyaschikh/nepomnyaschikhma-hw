package main.java.practice_4.for_tasks;

/**
 * Задача:
 * Напишите программу, которая выводит все числа от 1 до 100, которые делятся на 3.
 * Методы и конструкции:
 * for
 * оператор %
 * if
 * System.out.println()
 */

public class Task1 {
    public static void main(String[] args) {
        printNumbers();
    }

    private static void printNumbers() {
        for (int i = 1; i <= 100; i++) {
            if (i % 3 == 0) {
                System.out.println(i);
            }
        }
    }
}
