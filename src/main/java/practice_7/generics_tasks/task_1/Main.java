package main.java.practice_7.generics_tasks.task_1;

/**
 * Условие задачи: Разработайте дженерик класс Box, который может хранить объекты любого типа.
 * Класс должен иметь методы для установки и получения значения объекта, хранящегося внутри.
 */

public class Main {
    public static void main(String[] args) {
        Box<String> stringsBox = new Box<>();
        stringsBox.setElement("Строка");
        System.out.println(stringsBox.getElement());

        System.out.println();

        Box<Integer> integerBox = new Box<>();
        integerBox.setElement(1);
        System.out.println(integerBox.getElement());
    }
}
