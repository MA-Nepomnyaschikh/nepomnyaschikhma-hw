package main.java.practice_6.optional.task_4;

import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        trackVisits();

    }

    public static void trackVisits() {
        HashSet<String> set = new HashSet<>();
        Scanner sc = new Scanner(System.in);
        int totalCount = 0;

        while (true) {
            String url = sc.nextLine();

            if (url.equals("exit")) {
                break;
            }

            set.add(url);
            totalCount++;
        }

        sc.close();
        System.out.println(set.size());
        System.out.println(totalCount);
    }
}
