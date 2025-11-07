package practice_6.linked_hash_set.task_2;

import java.util.LinkedHashSet;

/**
 * Напишите метод, который добавляет элемент в LinkedHashSet, но не добавляет дубликаты.
 */

public class Main {
    public static void main(String[] args) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        addElement(set, "A");
        addElement(set, "A");
        addElement(set, "A");
        addElement(set, "B");
        addElement(set, "B");
        addElement(set, "D");

        System.out.println(set);
    }

    public static void addElement(LinkedHashSet<String> set, String element) {
        if (!set.contains(element)) {
            set.add(element);
        }
    }
}
