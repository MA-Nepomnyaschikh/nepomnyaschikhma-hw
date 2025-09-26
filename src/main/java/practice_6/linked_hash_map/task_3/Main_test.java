package main.java.practice_6.linked_hash_map.task_3;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main_test {
    public static void main(String[] args) {
        Map<Integer, String> map = new LinkedHashMap<>();

        for (int i = 1; i < 13; i++) {
            addElement(map, i, "Page " + i);
        }

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public static void addElement(Map<Integer, String> map, Integer key, String value) {
        if (map.size() >= 10) {
            Integer firstKey = map.keySet().iterator().next();
            map.remove(firstKey);
        }
        map.put(key, value);
    }
}
