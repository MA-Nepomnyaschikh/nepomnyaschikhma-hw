package practice_9.task_6;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        for (int i = 1; i < 11; i++) {
            queue.add(i);
        }

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Callable<Integer> task = () -> {
            int sum = 0;
            for (int i = 0; i < 3; i++) {
                Integer value = queue.poll();
                if (value == null) {
                    break;
                }
                System.out.println(value);
                sum += value;
            }
            return sum;
        };

        Future<Integer>[] results = new Future[3];
        for (int i = 0; i < results.length; i++) {
            results[i] = executor.submit(task);
        }

        executor.shutdown();

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
