package express_java.practice_7.exceptions_tasks.task_4.exceptions;

public class EmailValidationException extends RuntimeException {
  public EmailValidationException(String message) {
    super(message);
  }
}
