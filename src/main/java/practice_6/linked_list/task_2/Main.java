package practice_6.linked_list.task_2;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.add("Задача 1");
        list.add("Задача 2");
        list.add("Задача 3");
        System.out.println(list);
        getJobs(list);
        System.out.println(list);
    }

    public static void getJobs(LinkedList<String> list) {
        while (!list.isEmpty()) {
            System.out.println(list.poll());
        }
    }
}
