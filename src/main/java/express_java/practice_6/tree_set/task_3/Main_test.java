package express_java.practice_6.tree_set.task_3;

import java.util.TreeSet;

public class Main_test {
    public static void main(String[] args) {
        TreeSet<Integer> set = new TreeSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        set.add(10);

        findNearest(set, 0);
    }

    public static void findNearest(TreeSet<Integer> set, int x) {
        Integer min = set.lower(x);
        Integer max = set.higher(x);

        if (set.isEmpty()) {
            System.out.println("Множество не содержит элементов");
        } else if (min != null && max != null) {
            System.out.println("предыдущий = " + min + ", следующий = " + max);
        } else if (min == null && max != null) {
            System.out.println("следующий = " + max);
        } else if (min != null && max == null) {
            System.out.println("предыдущий = " + min);
        }
    }
}
