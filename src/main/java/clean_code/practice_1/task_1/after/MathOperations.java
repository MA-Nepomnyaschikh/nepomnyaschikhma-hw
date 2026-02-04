package clean_code.practice_1.task_1.after;

public class MathOperations {
    public int add(int a, int b) {
        return a + b;
    }
    public int add(int a, int b, int... args) {
        int sum = a + b;
        for (int x : args) {
            sum += x;
        }
        return sum;
    }
}


