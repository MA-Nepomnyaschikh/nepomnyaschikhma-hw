package main.java.practice_4.while_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая принимает с консоли число и выводит его факториал (произведение всех чисел от 1 до n), используя цикл while.
 * Методы и конструкции:
 * Scanner
 * while
 * переменные int result = 1, int i = 1
 * System.out.println()
 */

public class Task1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println(factorial(n));
    }

    private static int factorial(int number) {
        int sum = 0;
        int i = 1;
        while (i <= number) {
            sum += i;
            i++;
        }
        return sum;
    }
}
