package practice_7.exceptions_tasks.task_4;

import practice_7.exceptions_tasks.task_4.exceptions.EmailValidationException;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Напишите функцию, которая принимает строку в качестве аргумента и проверяет, является ли строка правильным электронным адресом.
 * Если строка не удовлетворяет критериям, функция должна выбрасывать непроверяемое исключение.
 */

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String email = sc.nextLine();
            checkEmail(email);
        } catch (EmailValidationException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void checkEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            System.out.println("Электронный адрес корректен");
        } else {
            throw new EmailValidationException("Указанный электронный адрес некорректен");
        }
    }
}
