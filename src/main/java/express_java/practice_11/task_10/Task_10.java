package express_java.practice_11.task_10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Код должен удалять элементы списка, но выбрасывает ConcurrentModificationException.
 */

public class Task_10 {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie"));

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (name.startsWith("A")) {
                names.remove(name);
            }
        }

//        Iterator<String> it = names.iterator();
//        while (it.hasNext()) {
//            String name = it.next();
//            if (name.startsWith("A")) {
//                it.remove();
//            }
//        }

        System.out.println(names);
    }
}
