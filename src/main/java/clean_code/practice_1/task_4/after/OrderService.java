package clean_code.practice_1.task_4.after;

public class OrderService {
    public void processOrder(Order order) {
        System.out.println("Обрабатываем заказ №" + order.getOrderNumber());
    }
}
