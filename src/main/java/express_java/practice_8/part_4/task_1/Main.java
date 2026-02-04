package express_java.practice_8.part_4.task_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Напишите программу, которая принимает список строк и группирует их по первой букве, используя Stream API.
 */

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("adsfg", "bsd2", "cfdhkss", "fghh", "esdfsfdff", "ff2g", "g", "fh"));
        System.out.println(groupByFirstLetter(list));
    }

    public static Map<String, List<String>> groupByFirstLetter(List<String> list) {
        return list.stream()
                .collect(Collectors.groupingBy(s -> s.substring(0, 1)));
    }

}
