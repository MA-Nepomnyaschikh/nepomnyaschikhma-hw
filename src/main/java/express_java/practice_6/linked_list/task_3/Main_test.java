package express_java.practice_6.linked_list.task_3;

import java.util.LinkedList;

public class Main_test {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.add("Задача 1");
        list.add("Задача 2");
        list.add("Задача 3");

        System.out.println(list.getFirst());
        System.out.println(list.getLast());
    }
}
