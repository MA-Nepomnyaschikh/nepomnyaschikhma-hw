package main.java.practice_4.if_else_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Программа принимает число от 1 до 5 и выводит текстовое описание оценки:
 * 5 — "Отлично"
 * 4 — "Хорошо"
 * 3 — "Удовлетворительно"
 * 2 или 1 — "Неудовлетворительно"
 * Методы и конструкции:
 * Scanner
 * switch-case или if-else if
 * System.out.println()
 */

public class Task3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        switch (x) {
            case 5:
                System.out.println("Отлично");
                break;
            case 4:
                System.out.println("Хорошо");
                break;
            case 3:
                System.out.println("Удовлетворительно");
                break;
            case 2:
                System.out.println("Неудовлетворительно");
                break;
            case 1:
                System.out.println("Неудовлетворительно");
                break;
        }
    }
}
