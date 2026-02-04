package express_java.practice_6.linked_list.task_4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new LinkedList<>(List.of(1,2,3,4,5,6,7,8,9));
        System.out.println(sumNumbersOfList(list));
    }

    public static int sumNumbersOfList(List<Integer> list) {
        int sum = 0;
        for (Integer i : list) {
            sum += i;
        }
        return sum;
    }
}
