package practice_12.task_2;

import practice_12.task_2.exceptions.InvalidUserException;

public class UserValidator {

    private static boolean flag = true;

    public void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (flag) {
            if (!name.matches("^[A-Z].*")) {
                throw new InvalidUserException("Invalid name");
            }
        }
    }

    public void validateEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (flag) {
            if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                throw new InvalidUserException("Invalid email");
            }
        }
    }

    public void validateAge(int age) {
        if (flag) {
            if (age < 18 || age > 100) {
                throw new InvalidUserException("Invalid age");
            }
        }
    }

    public static boolean isFlag() {
        return flag;
    }

    public static void setFlag(boolean flag) {
        UserValidator.flag = flag;
    }
}
