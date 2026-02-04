package express_java.practice_6.array_deque.task_2;

import java.util.ArrayDeque;

public class Main {
    public static void main(String[] args) {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        arrayDeque.push(1);
        arrayDeque.push(2);
        arrayDeque.push(3);
        arrayDeque.push(4);
        arrayDeque.push(5);

        while (!arrayDeque.isEmpty()) {
            System.out.println(arrayDeque.pop());
        }

    }
}
