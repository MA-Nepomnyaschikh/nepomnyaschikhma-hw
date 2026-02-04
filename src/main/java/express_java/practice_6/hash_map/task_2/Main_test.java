package express_java.practice_6.hash_map.task_2;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main_test {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Аня", 17);
        map.put("Даня", 23);
        map.put("Вика", 25);
        map.put("Леша", 19);
        map.put("Андрей", 21);

        checkName(map);
    }

    public static void checkName(Map<String, Integer> map) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        if (map.containsKey(name)) {
            System.out.println("Имя найдено");
        } else {
            System.out.println("Имя не найдено");
        }
        scanner.close();
    }
}
