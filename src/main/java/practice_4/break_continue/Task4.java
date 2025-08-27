package main.java.practice_4.break_continue;

import java.util.Scanner;

/**
 * Задача:
 * Программа должна запрашивать строки у пользователя и завершаться только тогда, когда введена команда "stop".
 * Методы и конструкции:
 * Scanner
 * while (true)
 * метод .equals("stop")
 * break
 * System.out.println()
 */

public class Task4 {
    public static void main(String[] args) {
        stopProgram();
    }

    public static void stopProgram() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите команду");
        String input;
        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            if (input.equals("stop")) {
                System.out.println("Программа закрыта");
                break;
            }
        }
    }
}
