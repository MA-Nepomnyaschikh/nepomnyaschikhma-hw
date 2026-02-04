package express_java.practice_6.array_list.task_2;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        list.forEach(number -> {
            if (number % 2 == 0) {
                System.out.print(number + " ");
            }
        });
    }
}
