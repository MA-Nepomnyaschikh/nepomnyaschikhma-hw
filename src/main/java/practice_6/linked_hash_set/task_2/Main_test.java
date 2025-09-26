package main.java.practice_6.linked_hash_set.task_2;

import java.util.LinkedHashSet;
import java.util.Set;

public class Main_test {
    public static void main(String[] args) {
        Set<String> set = new LinkedHashSet<>();
        set.add("test1");
        set.add("test2");
        set.add("test3");
        set.add("test4");
        set.add("test5");

        addUniqueElement(set, "test2");
        addUniqueElement(set, "test6");

        System.out.println(set);
    }

    public static void addUniqueElement(Set<String> set, String element) {
        if (!set.contains(element)) {
            set.add(element);
        }
    }
}
