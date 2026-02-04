package express_java.practice_6.linked_hash_set.task_1;

import java.util.LinkedHashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<String> set = new LinkedHashSet<>();
        set.add("test1");
        set.add("test2");
        set.add("test3");
        set.add("test4");
        set.add("test5");

        System.out.println(set);
    }
}
