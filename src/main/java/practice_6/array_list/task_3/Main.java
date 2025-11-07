package practice_6.array_list.task_3;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of("abc", "basd", "cd"));
        String longest = "";
        for (String s : list) {
            if (s.length() > longest.length()) {
                longest = s;
            }
        }
        System.out.println(longest);

    }
}
