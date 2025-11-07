package practice_6.optional.task_8;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine().toLowerCase();

        HashSet<String> set = new HashSet<>(List.of(s.split(" ")));
        System.out.println("Уникальных слов в строке: " + set.size());

        sc.close();
    }
}
