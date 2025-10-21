package main.java.practice_9.task_3;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Условие задачи: Создайте поток, который бесконечно увеличивает счетчик. В основном потоке через 2 секунды установите флаг stop = true, чтобы остановить поток.
 */

public class Main {
    public static volatile boolean stop = false;

    public static void main(String[] args) {
        task_3();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        stop = true;
    }

    public static void task_3() {
        AtomicInteger count = new AtomicInteger(0);
        Thread thread = new Thread(() -> {
            while (!stop) {
                count.incrementAndGet();
                System.out.println(count.get());
            }
        });

        thread.start();
    }
}
