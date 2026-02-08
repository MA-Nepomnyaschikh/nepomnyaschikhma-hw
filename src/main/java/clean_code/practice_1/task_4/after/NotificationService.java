package clean_code.practice_1.task_4.after;

public class NotificationService {
    public void sendEmailConfirmation(Order order) {
        System.out.println("Отправляем клиенту уведомление по заказу № " + order.getOrderNumber());
    }
}
