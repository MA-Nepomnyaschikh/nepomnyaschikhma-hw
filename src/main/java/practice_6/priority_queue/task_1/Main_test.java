package practice_6.priority_queue.task_1;

import java.util.PriorityQueue;

public class Main_test {
    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 5; i >= 1; i--) {
            pq.add(i);
        }

        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
    }
}
