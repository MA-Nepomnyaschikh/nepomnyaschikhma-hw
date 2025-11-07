package practice_4.break_continue;

/**
 * Задача:
 * Создайте программу, которая выводит числа от 1 до 20, пропуская те, которые делятся на 3.
 * Методы и конструкции:
 * for
 * if, оператор %
 * continue
 * System.out.println()
 */

public class Task2 {
    public static void main(String[] args) {
        printNumbers();
    }

    public static void printNumbers() {
        for (int i = 0; i <= 20; i++) {
            if (i % 3 == 0) {
                continue;
            }
            System.out.println(i);
        }
    }
}
