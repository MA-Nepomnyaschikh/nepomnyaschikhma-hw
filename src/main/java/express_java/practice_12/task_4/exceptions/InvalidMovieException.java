package express_java.practice_12.task_4.exceptions;

public class InvalidMovieException extends RuntimeException {
    public InvalidMovieException(String message) {
        super(message);
    }
}
