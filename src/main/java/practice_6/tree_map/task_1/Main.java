package main.java.practice_6.tree_map.task_1;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Map<String,Integer> map = new TreeMap<>();
        map.put("Anya", 5);
        map.put("Misha", 4);
        map.put("Dima", 5);
        map.put("Olya", 4);
        map.put("Zhenya", 3);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
