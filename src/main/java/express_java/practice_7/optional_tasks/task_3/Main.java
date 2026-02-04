package express_java.practice_7.optional_tasks.task_3;

public class Main {
    public static void main(String[] args) {
        NumberBox<Integer> box_1 = new NumberBox<>();
        NumberBox<Double> box_2 = new NumberBox<>();

        box_1.addElement(1);
        box_1.addElement(2);
        box_1.addElement(3);
        box_1.addElement(4);
        box_1.addElement(5);

        box_2.addElement(1.5);
        box_2.addElement(2.5);
        box_2.addElement(3.5);
        box_2.addElement(4.5);
        box_2.addElement(5.5);

        System.out.println(box_1.getSum());
        System.out.println(box_2.getSum());
    }
}
