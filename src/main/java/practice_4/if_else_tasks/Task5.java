package practice_4.if_else_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая определяет размер скидки:
 * До 18 лет — 25%
 * От 65 и старше — 30%
 * Остальные — без скидки
 * Методы и конструкции:
 * Scanner
 * if, else if, else
 * System.out.println()
 */

public class Task5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        if (x < 18) {
            System.out.println("25%");
        } else if (x >= 65) {
            System.out.println("30%");
        } else {
            System.out.println("Скидка не предусмотрена");
        }
    }
}
