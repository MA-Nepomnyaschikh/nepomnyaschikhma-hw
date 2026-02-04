package clean_code.practice_1.task_8.before;

class NotificationService {
    private EmailSender emailSender = new EmailSender();
    public void sendNotification(String message) {
        emailSender.sendEmail(message);
    }
}
