package express_java.practice_4.do_while_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая запрашивает у пользователя число и продолжает спрашивать, пока оно не станет положительным.
 * Методы и конструкции:
 * Scanner
 * do-while
 * System.out.println()
 * условие number <= 0
 */

public class Task1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        checkNumber(scanner);
        scanner.close();
    }

    public static void checkNumber(Scanner scanner) {
        int number;
        do {
            System.out.println("Введите число");
            number = scanner.nextInt();
        } while (number <= 0);
    }
}
