package express_java.practice_6.optional.task_6;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();

        map.put("Иван", "89090909091");
        map.put("Петр", "89090909092");
        map.put("Александр", "89090909093");
        map.put("Мария", "89090909094");
        map.put("Анна", "89090909095");

        System.out.println(map.get("Иван"));
        System.out.println(map.containsKey("Петр"));
    }
}
