package express_java.practice_4.if_else_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Программа принимает число — результат теста (0–100) и выводит:
 * 90 и выше — "Отлично"
 * 75–89 — "Хорошо"
 * 60–74 — "Удовлетворительно"
 * ниже 60 — "Неудовлетворительно"
 * Методы и конструкции:
 * Scanner
 * if, else if, else
 * System.out.println()
 */

public class Task6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        if (x > 0 && x <=100) {
            if (x >= 90) {
                System.out.println("Отлично");
            } else if (x >= 75 && x <= 89) {
                System.out.println("Хорошо");
            } else if (x >= 60 && x <= 74) {
                System.out.println("Удовлетворительно");
            } else {
                System.out.println("Неудовлетворительно");
            }
        }
    }
}
