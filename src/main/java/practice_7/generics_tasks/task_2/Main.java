package practice_7.generics_tasks.task_2;

/**
 * Условие задачи: Напишите дженерик метод printArray, который принимает массив элементов любого типа
 * и выводит каждый элемент массива на консоль.
 */

public class Main {
    public static void main(String[] args) {
        String[] arr1 = {"a", "b", "c", "d", "e", "f", "g", "h"};
        Integer[] arr2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        printArray(arr1);
        System.out.println();
        printArray(arr2);
    }

    public static <T> void printArray(T[] array) {
        for (T t : array) {
            System.out.println(t);
        }
    }
}
