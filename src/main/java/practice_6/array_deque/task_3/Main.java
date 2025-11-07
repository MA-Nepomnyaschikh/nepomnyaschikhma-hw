package practice_6.array_deque.task_3;

import java.util.ArrayDeque;

/**
 * Используйте ArrayDeque как очередь: добавьте элементы в начало и конец, извлеките из обоих концов.
 */

public class Main {
    public static void main(String[] args) {
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        queue.addFirst(1);
        queue.addLast(2);
        queue.addFirst(3);
        queue.addLast(4);
        queue.addFirst(5);
        queue.addLast(6);
        queue.addFirst(7);
        queue.addLast(8);
        queue.addFirst(9);
        queue.addLast(10);

        while (!queue.isEmpty()) {
            System.out.println(queue.removeLast());
            System.out.println(queue.removeFirst());
        }
    }
}
