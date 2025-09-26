package main.java.practice_6.linked_hash_map.task_1;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        map.put(1, 5);
        map.put(2, 4);
        map.put(3, 3);
        map.put(4, 2);
        map.put(5, 1);

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
