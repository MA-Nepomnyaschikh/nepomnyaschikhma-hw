package express_java.practice_8.part_3.task_4;

import java.util.ArrayList;
import java.util.List;

/**
 * Напишите программу, которая принимает список строк и находит первую строку, начинающуюся на букву "Б", используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("adsfg", "bsd", "cfdhkss", "fghh", "bsddsas", "esdfsfdff", "ffg", "g", "fh"));
        System.out.println(getStringStartsWith(list, "b"));
    }

    public static String getStringStartsWith(List<String> list, String prefix) {
        return list.stream()
                .filter(s -> s.startsWith(prefix))
                .findFirst()
                .orElse("В списке отсутствуют строки, которые начинаются на символ " + prefix);
    }
}
