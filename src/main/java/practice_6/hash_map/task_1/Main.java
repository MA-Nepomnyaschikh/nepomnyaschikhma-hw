package practice_6.hash_map.task_1;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Аня", 17);
        map.put("Даня", 23);
        map.put("Вика", 25);
        map.put("Леша", 19);
        map.put("Андрей", 21);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
