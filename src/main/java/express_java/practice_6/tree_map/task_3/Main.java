package express_java.practice_6.tree_map.task_3;

import java.util.Scanner;
import java.util.TreeMap;

/**
 * Реализуйте TreeMap, который хранит сотрудников и их ID, с возможностью поиска ближайшего большего ID.
 */

public class Main {
    public static void main(String[] args) {
        TreeMap<Integer, String> employees = new TreeMap<>();

        for (int i = 1; i < 11; i++) {
            employees.put(i, "Name " + i);
        }

        showNextEmployee(employees);
    }

    public static void showNextEmployee(TreeMap<Integer, String> employees) {
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        Integer nextId = employees.higherKey(id);
        if (nextId != null) {
            System.out.println(nextId);
        } else {
            System.out.println("ID " + id + " является последним в словаре");
        }
    }
}
