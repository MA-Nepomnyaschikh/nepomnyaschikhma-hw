package express_java.practice_4.while_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Реализуйте программу, которая выводит все чётные числа от 1 до числа, введённого пользователем.
 * Методы и конструкции:
 * Scanner
 * while
 * if, оператор %
 * System.out.println()
 */

public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        print(n);
    }

    private static void print(int number) {
        int i = 1;
        while (i <= number) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
            i++;
        }
    }
}
