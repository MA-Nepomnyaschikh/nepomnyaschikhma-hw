package practice_6.priority_queue.task_1;

import java.util.PriorityQueue;

/**
 * Создайте PriorityQueue и добавьте 5 чисел. Выведите их в порядке удаления.
 */

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int i = 5; i > 0; i--) {
            System.out.println(i);
            pq.add(i);
        }

        System.out.println();

        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
    }
}
