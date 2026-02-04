package express_java.practice_6.optional.task_2;

import java.util.Deque;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        Deque<String> queue = new LinkedList<>();

        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        queue.offer("e");

        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }
}
