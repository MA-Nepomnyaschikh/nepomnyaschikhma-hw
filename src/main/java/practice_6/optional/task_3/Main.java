package main.java.practice_6.optional.task_3;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Map<String, String> settings = new HashMap<>();
        settings.put("a", "b");
        settings.put("c", "d");
        settings.put("e", "f");
        settings.put("g", "h");
        settings.put("i", "j");

        checkSettings(settings);
    }
    public static void checkSettings(Map<String, String> map) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (map.containsKey(input)) {
            System.out.println("Значение для указанного параметра: " + map.get(input));
        } else {
            System.out.println("Указанный параметр отсутствует в настройках");
        }
    }
}
