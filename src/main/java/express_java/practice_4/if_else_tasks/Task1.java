package express_java.practice_4.if_else_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая принимает с консоли число и проверяет:
 * Если число больше 0, выведите "Число положительное".
 * Если меньше 0 — "Число отрицательное".
 * Если равно 0 — "Число равно нулю".
 * Методы и конструкции:
 * Scanner
 * if, else if, else
 * System.out.println()
 */

public class Task1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        if (x > 0) {
            System.out.println("Число положительное");
        } else if (x < 0) {
            System.out.println("Число отрицательное");
        } else {
            System.out.println("Число равно нулю");
        }
    }
}
