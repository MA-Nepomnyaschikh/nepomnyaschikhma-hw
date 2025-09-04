package main.java.practice_4.switch_tasks;

import java.util.Scanner;

/**
 * Задача:
 * Программа принимает два числа и оператор (+, -, *, /), затем выполняет операцию и выводит результат.
 * Методы и конструкции:
 * Scanner
 * switch-case
 * if для проверки деления на 0
 * System.out.println()
 * Арифметические операторы +, -, *, /
 */

public class Task5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        String s = scanner.next();
        System.out.println(calculate(x , y , s));
    }

    private static int calculate(int x, int y, String s) {
        int sum = 0;
        switch (s) {
            case "+":
                sum = x + y;
                break;
            case "-":
                sum = x - y;
                break;
            case "*":
                sum = x * y;
                break;
            case "/":
                if (x == 0 || y == 0){
                    System.out.println("Числа не должны быть равны нулю");
                    break;
                }
                sum = x / y;
                break;
        }
        return sum;
    }
}
