package main.java.practice_4.switch_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Программа принимает число от 0 до 100 и переводит его в буквенную оценку по шкале:
 * 90–100 — A
 * 80–89 — B
 * 70–79 — C
 * 60–69 — D
 * ниже 60 — F
 * Методы и конструкции:
 * Scanner
 * if-else if или switch-case
 * System.out.println()
 */

public class Task3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        checkSymbol(x);

    }

    private static void checkSymbol(int x) {
        if (x >= 0 && x <= 100) {
            if (x >= 90) {
                System.out.println("A");
            } else if (x >= 70 && x <= 79) {
                System.out.println("B");
            } else if (x >= 60 && x <= 69) {
                System.out.println("D");
            } else {
                System.out.println("F");
            }
        } else {
            System.out.println("Неизвестный символ");
        }

    }
}
