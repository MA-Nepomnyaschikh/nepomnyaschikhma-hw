package express_java.practice_4.if_else_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая проверяет, является ли введённое число чётным или нечётным.
 * Методы и конструкции:
 * Scanner
 * if-else, оператор %
 * System.out.println()
 */

public class Task4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        if (x % 2 == 0) {
            System.out.println("Четное");
        } else {
            System.out.println("Нечетное");
        }

    }
}
