package express_java.practice_4.switch_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Реализуйте программу, которая принимает с консоли число от 1 до 7 и выводит название соответствующего дня недели (например, 1 — "Понедельник").
 * Методы и конструкции:
 * Scanner
 * switch-case
 * System.out.println()
 * default (для обработки некорректного ввода)
 */

public class Task1 {
    public static void main(String[] args) {
        checkDay();
    }

    public static void checkDay() {
        Scanner scanner = new Scanner(System.in);
        int day = scanner.nextInt();
        switch (day) {
            case 1:
                System.out.println("Понедельник");
                break;
            case 2:
                System.out.println("Вторник");
                break;
            case 3:
                System.out.println("Среда");
                break;
            case 4:
                System.out.println("Четверг");
                break;
            case 5:
                System.out.println("Пятница");
                break;
            case 6:
                System.out.println("Суббота");
                break;
            case 7:
                System.out.println("Воскресенье");
                break;
            default:
                System.out.println("Несуществующий день");
                break;
        }
    }
}
