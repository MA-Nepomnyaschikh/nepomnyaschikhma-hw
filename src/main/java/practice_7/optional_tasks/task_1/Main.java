package practice_7.optional_tasks.task_1;

public class Main {
    public static void main(String[] args) {
        Storage<String> storage_1 = new Storage<>();
        storage_1.setElement("One");
        System.out.println(storage_1.getElement());

        System.out.println();

        Storage<Integer> storage_2 = new Storage<>();
        storage_2.setElement(1);
        System.out.println(storage_2.getElement());

    }
}
