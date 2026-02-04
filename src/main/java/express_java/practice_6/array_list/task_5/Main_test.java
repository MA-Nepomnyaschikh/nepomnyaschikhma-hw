package express_java.practice_6.array_list.task_5;

import java.util.ArrayList;
import java.util.List;

public class Main_test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9));
        System.out.println(getMaxFromList(list));
    }

    private static int getMaxFromList(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        for (Integer i : list) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }
}
