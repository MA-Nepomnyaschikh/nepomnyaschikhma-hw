package practice_6.tree_set.task_3;

import java.util.Scanner;
import java.util.TreeSet;

/**
 * Найдите ближайшее большее и меньшее число к заданному в TreeSet.
 */

public class Main {
    public static void main(String[] args) {
        TreeSet<Integer> set = new TreeSet<>();

        for (int i = 0; i < 11; i++) {
            set.add(i);
        }

        findNearest(set);
    }

    public static void findNearest(TreeSet<Integer> set) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();

        Integer max = set.higher(x);
        Integer min = set.lower(x);

        if (set.isEmpty()) {
            System.out.println("Empty set");
        } else {
            if (max != null && min != null) {
                System.out.println("Следующий: " + max + ", предыдущий: " + min);
            } else if (max == null) {
                System.out.println("Предыдущий: " + min);
            } else if (min == null) {
                System.out.println("Следующий: " + max);
            }
        }

        sc.close();
    }
}
