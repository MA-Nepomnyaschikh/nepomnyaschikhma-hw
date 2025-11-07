package practice_7.exceptions_tasks.task_2;

import java.util.Scanner;

/**
 * Условие задачи: Напишите метод, который принимает на вход два числа и выполняет их деление.
 * Обработайте ситуацию, когда второе число равно нулю, чтобы избежать исключения при делении.
 */

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            System.out.println(x / y);
        } catch (ArithmeticException e) {
            System.out.println("Делить на 0 нельзя");
        }
    }
}
