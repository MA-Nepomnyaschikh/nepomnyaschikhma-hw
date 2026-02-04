package express_java.practice_4.do_while_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Реализуйте программу, которая запрашивает у пользователя пароль, пока он не введёт верный (пароль заранее задан в коде).
 * Методы и конструкции:
 * Scanner
 * do-while
 * метод .equals() для строк
 * System.out.println()
 */

public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        checkPassword(scanner);
        scanner.close();
    }

    public static void checkPassword(Scanner scanner) {
        String userPassword = "qwerty";
        String password;
        do {
            System.out.println("Введите пароль");
            password = scanner.nextLine();
        } while (!password.equals(userPassword));
    }
}
