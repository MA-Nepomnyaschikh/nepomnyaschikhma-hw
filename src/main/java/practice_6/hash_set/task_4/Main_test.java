package practice_6.hash_set.task_4;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main_test {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("Anya");
        set.add("Dima");
        set.add("Tolya");
        set.add("Tanya");
        set.add("Natasha");
        set.add("Igor");
        set.add("Eva");

        checkName(set);
    }

    public static void checkName(Set<String> set) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();

        if (set.contains(name)) {
            System.out.println("Имя " + name + " содержится в множестве");
        } else {
            System.out.println("Имя " + name + " отсутствует в множестве");
        }

        sc.close();
    }
}
