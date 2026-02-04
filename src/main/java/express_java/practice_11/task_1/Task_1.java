package express_java.practice_11.task_1;

/**
 * Код должен напечатать все элементы массива, но вместо этого выбрасывает исключение
 */

 public class Task_1 {
    public static void main(String[] args) {
        int[] numbers = {10, 20, 30, 40, 50};
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }
    }
}
