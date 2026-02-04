package express_java.practice_4.for_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая проверяет, является ли введённое число простым. Простое число делится только на 1 и на само себя.
 * Методы и конструкции:
 * Scanner
 * for
 * if-else
 * логическая переменная (boolean isPrime = true)
 * System.out.println()
 *
 * Cчитай число. Запусти цикл от 2 до number - 1.
 * Если число делится на любой из этих делителей (% == 0), оно не простое.
 * Если ни один делитель не найден — число простое.
 */

public class Task4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println(isPrime(n));
    }

    private static boolean isPrime(int number) {
        boolean isPrime = true;
        for (int i = 2; i <= number - 1; i++) {
            if (number % i == 0) {
                isPrime = false;
                break;
            }
        }
        return isPrime;
    }
}
