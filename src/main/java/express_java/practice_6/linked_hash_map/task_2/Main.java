package express_java.practice_6.linked_hash_map.task_2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("Igor", 89090909090L);
        map.put("Ivan", 89090809090L);
        map.put("Dmitriy", 89090709090L);
        map.put("Anna", 89090609090L);
        map.put("Olga", 89090509090L);

        getPhoneNumber(map);
    }

    public static void getPhoneNumber(Map<String, Long> map) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        if (map.containsKey(name)) {
            System.out.println(map.get(name));
        } else {
            System.out.println("Контакт не найден");
        }
        scanner.close();
    }
}
