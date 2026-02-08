package clean_code.practice_1.task_4.after;

public class InvoiceService {
    public void generateInvoice(Order order) {
        System.out.println("Генерируем счет по заказу № " + order.getOrderNumber() + " на сумму " + order.getTotalPrice());
    }
}
