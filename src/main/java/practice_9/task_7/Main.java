package main.java.practice_9.task_7;

public class Main {
    public static void main(String[] args) {
        Account account1 = new Account(1000000);
        Account account2 = new Account(1000000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                account1.transfer(account2, 10);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                account2.transfer(account1, 10);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(account1);
        System.out.println(account2);


    }
}
