package main.java.practice_4.break_continue;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая принимает ряд чисел от пользователя и выводит только положительные. Отрицательные числа нужно пропускать.
 * Методы и конструкции:
 * Scanner
 * while или do-while
 * if, continue
 * System.out.println()
 */

public class Task3 {
    public static void main(String[] args) {
        printNumbers();
    }

    public static void printNumbers() {
        Scanner sc = new Scanner(System.in);
        int number;
        int i = 0;
        while (sc.hasNextInt()) {
            number = sc.nextInt();
            if (number < 0) {
                i++;
                continue;
            }
            System.out.println(number);
            i++;
            if (i == 10) {
                break;
            }
        }
    }
}
