package clean_code.practice_1.task_8.after;

public class Main {
    public static void main(String[] args) {
        EmailSender emailSender = new EmailSender();
        NotificationService notificationService = new NotificationService(emailSender);
        notificationService.sendNotification("тест");
    }
}
