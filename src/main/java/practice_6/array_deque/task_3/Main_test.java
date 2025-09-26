package main.java.practice_6.array_deque.task_3;

import java.util.ArrayDeque;

public class Main_test {
    public static void main(String[] args) {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        arrayDeque.addFirst(1);
        arrayDeque.addLast(2);
        arrayDeque.addFirst(3);
        arrayDeque.addLast(4);
        arrayDeque.addFirst(5);
        arrayDeque.addLast(6);

        while (!arrayDeque.isEmpty()) {
            System.out.println(arrayDeque.removeFirst());
            System.out.println(arrayDeque.removeLast());
        }

    }
}
