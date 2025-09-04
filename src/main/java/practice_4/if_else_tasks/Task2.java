package main.java.practice_4.if_else_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Создайте программу, которая принимает два числа и выводит наибольшее из них.
 * Методы и конструкции:
 * Scanner
 * if-else или Math.max()
 * System.out.println()
 */

public class Task2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();

        if (a == b) {
            System.out.println(a);
        } else if (a > b) {
            System.out.println(a);
        } else {
            System.out.println(b);
        }

    }
}
