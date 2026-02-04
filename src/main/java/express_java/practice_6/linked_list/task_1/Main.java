package express_java.practice_6.linked_list.task_1;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>(List.of("a", "b", "c", "d", "e"));
        System.out.println(list);
        list.forEach(System.out::println);
    }
}
