package main.java.practice_4.do_while_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая будет ждать ввода команды "exit" от пользователя для завершения работы.
 * Методы и конструкции:
 * Scanner
 * do-while
 * метод .equals()
 * System.out.println()
 */

public class Task4 {
    public static void main(String[] args) {
        exitProgram();
    }

    public static void exitProgram() {
        Scanner scanner = new Scanner(System.in);
        String exitCommand = "exit";
        String input;
        do {
            System.out.println("Введите команду");
            input = scanner.nextLine();
        } while (!input.equals(exitCommand));
        System.out.println("Программа закрыта");
        scanner.close();
    }
}
