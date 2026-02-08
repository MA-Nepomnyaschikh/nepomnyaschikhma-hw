package clean_code.practice_1.task_1.after;

public class MathOperations {
    public int add(int... args) {
        int sum = 0;
        for (int x : args) {
            sum += x;
        }
        return sum;
    }
}


