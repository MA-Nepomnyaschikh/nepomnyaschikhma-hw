package clean_code.practice_1.task_8.after;

public class Main {
    public static void main(String[] args) {
        NotificationSender sender = new EmailSender();
        NotificationService notificationService = new NotificationService(sender);
        notificationService.sendNotification("тест");
    }
}
