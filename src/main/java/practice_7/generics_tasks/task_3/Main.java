package main.java.practice_7.generics_tasks.task_3;

/**
 * Условие задачи: Создайте класс Pair, который может хранить два объекта разных типов.
 * Класс должен позволять получать и устанавливать каждый из этих объектов.
 */

public class Main {
    public static void main(String[] args) {
        Pair<String, Integer> pair1 = new Pair<>();
        pair1.setKey("key");
        pair1.setValue(3);
        System.out.println(pair1.getKey() + " " + pair1.getValue());

        System.out.println();

        Pair<Integer, String> pair2 = new Pair<>();
        pair2.setKey(2);
        pair2.setValue("value");
        System.out.println(pair2.getKey() + " " + pair2.getValue());
    }
}
