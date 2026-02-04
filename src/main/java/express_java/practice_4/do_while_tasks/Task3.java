package express_java.practice_4.do_while_tasks;

/**
 * Задача:
 * Создайте программу, которая выводит числа от 1 до 10, используя do-while.
 * Методы и конструкции:
 * переменная-счётчик i
 * do-while
 * System.out.println()
 */

public class Task3 {
    public static void main(String[] args) {
        print();
    }

    public static void print() {
        int i = 1;
        do {
            System.out.println(i);
            i++;
        } while (i <= 10);
    }
}
