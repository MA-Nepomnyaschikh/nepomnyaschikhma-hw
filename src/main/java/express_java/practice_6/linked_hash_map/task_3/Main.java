package express_java.practice_6.linked_hash_map.task_3;

import java.util.LinkedHashMap;

/**
 * Создайте LinkedHashMap, который хранит историю просмотров пользователя (максимум 10 элементов).
 */

public class Main {
    public static void main(String[] args) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();

        addElement(map,1, "A");
        addElement(map,2, "B");
        addElement(map,3, "C");
        addElement(map,4, "D");
        addElement(map,5, "E");
        addElement(map,6, "F");
        addElement(map,7, "G");
        addElement(map,8, "H");
        addElement(map,9, "I");
        addElement(map,10, "J");
        addElement(map,11, "K");
        addElement(map,12, "L");

        System.out.println(map);
    }

    public static void addElement(LinkedHashMap<Integer, String> map, Integer key, String value) {
        if (map.size() == 10) {
            Integer firstKey = map.keySet().iterator().next();
            map.remove(firstKey);
        }
        map.put(key, value);
    }
}
