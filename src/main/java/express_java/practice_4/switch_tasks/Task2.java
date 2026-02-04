package express_java.practice_4.switch_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Определите стоимость билета в кино:
 * будние дни (1–5) — 300 рублей
 * выходные (6–7) — 450 рублей
 * Методы и конструкции:
 * Scanner
 * switch-case (можно несколько case подряд) или if-else
 * System.out.println()
 */

public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int day = scanner.nextInt();
        checkPrice(day);
    }

    public static void checkPrice(int day) {
        switch (day) {
            case 1, 2, 3, 4, 5:
                System.out.println("300 рублей");
                break;
            case 6, 7:
                System.out.println("450 рублей");
                break;
            default:
                System.out.println("Несуществующий день");
                break;
        }
    }


}
