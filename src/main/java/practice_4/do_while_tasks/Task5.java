package practice_4.do_while_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Напишите программу, которая подсчитывает, сколько цифр в целом числе, введённом пользователем.
 * Методы и конструкции:
 * Scanner
 * do-while
 * деление / 10
 * count++
 * условие для 0
 */

public class Task5 {
    public static void main(String[] args) {
        countNumbers();
    }

    public static void countNumbers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите число");
        int number = scanner.nextInt();;
        int count = 0;
        do {
            number = number / 10;
            count++;
        } while (number != 0);
        System.out.println(count);
        scanner.close();
    }
}
