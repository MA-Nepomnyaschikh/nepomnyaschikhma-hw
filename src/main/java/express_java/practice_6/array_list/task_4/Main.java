package express_java.practice_6.array_list.task_4;

import java.util.ArrayList;
import java.util.List;

/**
 * Создайте ArrayList из целых чисел. Напишите программу, которая вычисляет и выводит сумму всех чисел в списке.
 */

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            list.add(i);
        }
        System.out.println(sumNumbers(list));
    }

    public static Integer sumNumbers(List<Integer> list) {
        int sum = 0;
        for (Integer i : list) {
            sum = sum + i;
        }
        return sum;
    }
}
