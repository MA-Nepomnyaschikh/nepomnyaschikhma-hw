package clean_code.practice_1.task_1.after;

public class Main {
    public static void main(String[] args) {
        MathOperations mathOperations = new MathOperations();
        System.out.println(mathOperations.add(5, 5));
        System.out.println(mathOperations.add(5, 5, 5));
        System.out.println(mathOperations.add(5, 5, 5, 5));
    }
}
