package express_java.practice_11.task_4;

/**
 * Код должен проверить, является ли строка палиндромом, но выбрасывает NullPointerException.
 */

public class Task_4 {
    public static void main(String[] args) {
        System.out.println(isPalindrome(null));
    }
    public static boolean isPalindrome(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        String reversed = new StringBuilder(str).reverse().toString();
        return str.equals(reversed);
    }
}
