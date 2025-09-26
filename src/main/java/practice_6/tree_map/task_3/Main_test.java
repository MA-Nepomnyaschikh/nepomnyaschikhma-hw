package main.java.practice_6.tree_map.task_3;

import java.util.Scanner;
import java.util.TreeMap;

public class Main_test {
    public static void main(String[] args) {
        TreeMap<Integer, String> employees = new TreeMap<>();
        employees.put(1, "Anya");
        employees.put(2, "Misha");
        employees.put(3, "Dima");
        employees.put(4, "Olya");
        employees.put(5, "Zhenya");

        showNextEmployee(employees);
    }

    public static void showNextEmployee(TreeMap<Integer, String> employees) {
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        Integer nextId = employees.higherKey(id);

        if (nextId != null) {
            System.out.println("ID следующего сотрудника: " + nextId + ", имя сотрудника: " + employees.get(nextId));
        } else {
            System.out.println("Cотрудника с более высоким ID нет");
        }
    }
}
