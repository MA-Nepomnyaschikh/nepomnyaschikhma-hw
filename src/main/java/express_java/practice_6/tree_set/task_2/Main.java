package express_java.practice_6.tree_set.task_2;

import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        Set<Integer> set = new TreeSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);

        addUniqueElement(set, 5);
        addUniqueElement(set, 6);

        System.out.println(set);
    }

    public static void addUniqueElement(Set<Integer> set, Integer element) {
        if (!set.contains(element)) {
            set.add(element);
        }
    }
}
