package main.java.practice_4.break_continue;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая запрашивает у пользователя числа и выводит их сумму. Программа завершает выполнение, если пользователь вводит отрицательное число.
 * Методы и конструкции:
 * Scanner
 * while (true)
 * break
 * переменная sum
 * System.out.println()
 */

public class Task1 {
    public static void main(String[] args) {
        sumNumbers();
    }

    public static void sumNumbers() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите число");
        int sum = 0;
        int number;
        while (sc.hasNextInt()) {
            number = sc.nextInt();
            if (number < 0) {
                break;
            }
            sum += number;
            System.out.println(sum);
        }
        System.out.println("Программа закрыта");
    }
}
