package clean_code.practice_3.task_2.exceptions;

public class UnsupportedFormatException extends RuntimeException {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}
