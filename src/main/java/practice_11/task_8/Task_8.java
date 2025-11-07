package practice_11.task_8;

/**
 * Код должен сравнить два числа, но почему-то результат не соответствует ожиданиям.
 */

public class Task_8 {
    public static void main(String[] args) {
        double a = Math.round((0.1 * 3) * 10) / 10.0;
        double b = 0.3;
        if (a == b) {
            System.out.println("Equal");
        } else {
            System.out.println("Not Equal");
        }
    }
}
