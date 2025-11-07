package practice_6.optional.task_1;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 11; i++) {
            set.add(i);
        }
        checkId(set);
    }

    public static void checkId(Set<Integer> set) {
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        if (set.contains(id)) {
            System.out.println("В системе зарегистрирован пользователь с указанным идентификатором");
        } else {
            System.out.println("Пользователь с указанным идентификатором не зарегистрирован в системе");
        }
    }
}
