package express_java.practice_8.part_1.task_2;

public class Main {
    public static void main(String[] args) {
        Runnable runnable_1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from anonymous class!");
            }
        };

        Runnable runnable_2 = () -> System.out.println("Hello from anonymous class!");

        runnable_1.run();
        runnable_2.run();
    }
}
