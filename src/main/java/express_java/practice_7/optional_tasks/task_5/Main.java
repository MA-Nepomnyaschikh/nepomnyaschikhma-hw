package express_java.practice_7.optional_tasks.task_5;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Integer> intList = new java.util.ArrayList<>();
        List<Number> numList = new java.util.ArrayList<>();
        List<Object> objList = new java.util.ArrayList<>();

        addNumbers(intList);
        addNumbers(numList);
        addNumbers(objList);

        System.out.println(intList);
        System.out.println(numList);
        System.out.println(objList);

    }

    public static void addNumbers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }
}
