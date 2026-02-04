package express_java.practice_6.hash_set.task_3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main_test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("G");
        list.add("H");
        list.add("I");
        list.add("I");
        list.add("J");
        list.add("K");
        Set<String> set = getSetFromList(list);
        System.out.println(set);

    }

    public static Set<String> getSetFromList(List<String> list) {
        return new HashSet<>(list);
    }
}
