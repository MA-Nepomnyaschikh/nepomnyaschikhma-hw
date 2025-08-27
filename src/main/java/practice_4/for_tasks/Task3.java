package main.java.practice_4.for_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Создайте программу, которая выводит таблицу умножения для числа, введённого пользователем.
 * Методы и конструкции:
 * Scanner
 * for
 * System.out.println()
 * арифметические операторы
 */

public class Task3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        table(n);
    }

    private static void table(int number) {
        for (int i = 1; i <= 10; i++) {
            System.out.println(number + " x " + i + " = " + (number * i));
        }
    }
}
