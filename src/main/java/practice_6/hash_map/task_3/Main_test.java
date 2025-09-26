package main.java.practice_6.hash_map.task_3;

import java.util.HashMap;
import java.util.Map;

public class Main_test {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Аня", 17);
        map.put("Яна", 15);
        map.put("Даня", 23);
        map.put("Вика", 25);
        map.put("Леша", 19);
        map.put("Андрей", 21);

        printUsersUnder18(map);
    }

    public static void printUsersUnder18(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() < 18) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }
}
