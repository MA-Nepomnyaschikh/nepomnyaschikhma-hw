package express_java.practice_6.tree_map.task_2;

import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        TreeMap<String,Integer> map = new TreeMap<>();
        map.put("Anya", 5);
        map.put("Misha", 4);
        map.put("Dima", 5);
        map.put("Olya", 4);
        map.put("Zhenya", 3);

        System.out.println(map.firstKey());
        System.out.println(map.lastKey());
    }
}
