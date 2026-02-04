package express_java.practice_8.part_2.task_1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Напишите программу, которая принимает список строк и удаляет из него все строки длиной 5 символов и менее, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("adsfg", "bsd", "cfdhkss", "fghh", "esdfsfdff", "ffg", "g", "fh"));
        System.out.println(filterStringsLongerThan(5, list));
    }

    public static List<String> filterStringsLongerThan(int limit, List<String> strings) {
        return strings.stream()
                .filter(s -> s.length() > limit)
                .collect(Collectors.toList());
    }
}
