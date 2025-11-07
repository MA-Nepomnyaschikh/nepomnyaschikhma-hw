package practice_7.exceptions_tasks.task_3;

import practice_7.exceptions_tasks.task_3.exceptions.AgeValidationException;

import java.util.Scanner;

/**
 * Разработайте метод, который проверяет валидность возраста пользователя.
 * Если возраст меньше 0 или больше 150, метод должен выбрасывать проверяемое исключение.
 */

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int age = scanner.nextInt();
            checkAge(age);
        } catch (AgeValidationException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void checkAge(int age) throws AgeValidationException {
        if (age >= 0 && age <= 150) {
            System.out.println("Возраст корректен");
        } else {
            throw new AgeValidationException("Указанный возраст некорректен");
        }
    }
}
