package practice_6.linked_list.task_5;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Используйте ListIterator для прохода по LinkedList в обоих направлениях.
 */

public class Main {
    public static void main(String[] args) {
        List<String> list = new LinkedList<>(List.of("test1", "test2","test3","test4","test5","test6"));

        ListIterator<String> iterator = list.listIterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println();
        while (iterator.hasPrevious()) {
            System.out.println(iterator.previous());
        }
    }
}
