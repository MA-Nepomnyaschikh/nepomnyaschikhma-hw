package main.java.practice_6.array_list.task_4;

import java.util.ArrayList;
import java.util.List;

public class Main_test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9));
        System.out.println(sumNumbers(list));
    }

    public static int sumNumbers(List<Integer> list) {
        int sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        return sum;
    }
}
