package practice_4.switch_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Программа читает строку: "start", "stop", "restart" или "status". Для каждой команды нужно вывести соответствующее сообщение, например: "Система запущена".
 * Методы и конструкции:
 * Scanner (nextLine())
 * switch-case по String
 * System.out.println()
 * default
 */

public class Task4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        printSystemMessage(input);
    }

    private static void printSystemMessage(String message) {
        switch (message) {
            case "start":
                System.out.println("Система запущена");
                break;
            case "stop":
                System.out.println("Система остановлена");
                break;
            case "restart":
                System.out.println("Система перезапускается");
                break;
            case "status":
                System.out.println("Система активна");
                break;
            default:
                System.out.println("Неизвестная команда");
                break;
        }
    }
}
