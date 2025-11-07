package practice_4.while_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Создайте программу, которая принимает с консоли положительное число и выводит обратный отсчёт от него до 1.
 * Методы и конструкции:
 * Scanner
 * while
 * System.out.println()
 * декремент number--
 */

public class Task3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        print(n);
    }

    private static void print(int number) {
        if (number > 0) {
            int x = number;
            int y = 1;
            while (x >= y) {
                System.out.println(x);
                x--;
            }
        } else {
            System.out.println("Некорректное число");
        }
    }
}
