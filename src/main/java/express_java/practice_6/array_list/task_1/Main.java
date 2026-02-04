package express_java.practice_6.array_list.task_1;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        System.out.println(list);
        list.add(6);
        System.out.println(list);
    }
}
