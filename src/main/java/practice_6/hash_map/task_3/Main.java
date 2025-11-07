package practice_6.hash_map.task_3;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализуйте метод, который печатает из HashMap всех пользователей младше 18 лет.
 */

public class Main {
    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>();

        map.put(18, "Аня");
        map.put(22, "Дима");
        map.put(15, "Миша");
        map.put(23, "Маша");
        map.put(17, "Яна");

        printUsersUnder18(map);
    }

    public static void printUsersUnder18(HashMap<Integer, String> map) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getKey() < 18) {
                System.out.println("Имя: " + entry.getValue() + ", Возраст: " + entry.getKey());
            }
        }
    }
}
