package practice_6.hash_map.task_2;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Проверьте, есть ли определённое имя в HashMap.
 */

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("Аня", 18);
        map.put("Дима", 22);
        map.put("Миша", 27);
        map.put("Маша", 23);
        map.put("Яна", 17);

        checkName(map);
    }

    public static void checkName(HashMap<String, Integer> map) {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();

        if (map.containsKey(name)) {
            System.out.println("Имя \"" + name + "\" содержится в словаре");
        } else {
            System.out.println("Имя \"" + name + "\" отсутствует в словаре");
        }
    }
}
