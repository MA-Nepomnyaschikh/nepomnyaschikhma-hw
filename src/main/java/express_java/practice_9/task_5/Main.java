package express_java.practice_9.task_5;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static volatile AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);

        Callable<Integer> task = () -> {
            System.out.println(Thread.currentThread().getName() + " начал выполнение задачи");
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " завершил выполнение задачи");
            return counter.incrementAndGet();
        };

        Future<Integer>[] results = new Future[6];
        for (int i = 0; i < results.length; i++) {
            results[i] = pool.submit(task);
        }

        pool.shutdown();

        int total = 0;

        for (Future<Integer> i : results) {
            try {
                total += i.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(total);

    }
}
