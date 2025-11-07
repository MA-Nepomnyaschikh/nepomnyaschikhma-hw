package practice_4.for_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Реализуйте программу, которая принимает с консоли число n и вычисляет сумму всех чисел от 1 до n.
 * Методы и конструкции:
 * Scanner
 * for
 * переменная int sum = 0
 * System.out.println()
 */

public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println(factorial(n));
    }

    private static int factorial(int number) {
        int sum = 0;
        for (int i = 1; i <= number; i++) {
            sum += i;
        }
        return sum;
    }
}
