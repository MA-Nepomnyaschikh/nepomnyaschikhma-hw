package clean_code.practice_1.task_8.after;

class EmailSender implements NotificationSender{
    @Override
    public void send(String message) {
        System.out.println("Отправка email: " + message);
    }
}
