package express_java.practice_8.part_1.task_1;

public class Main {
    public static void main(String[] args) {
        MathOperation summation = (a, b) -> a + b;
        MathOperation subtraction = (a, b) -> a - b;
        MathOperation multiplication = (a, b) -> a * b;
        MathOperation division = (a, b) -> a / b;

        System.out.println(summation.operation(4, 4));
        System.out.println(subtraction.operation(4, 4));
        System.out.println(multiplication.operation(4, 4));
        System.out.println(division.operation(4, 4));
    }
}
