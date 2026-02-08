package clean_code.practice_1.task_4.after;

public class Main {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        Order order = new Order(435, 5, 23456.50);
        orderService.processOrder(order);
        InvoiceService invoiceService = new InvoiceService();
        invoiceService.generateInvoice(order);
        NotificationService notificationService = new NotificationService();
        notificationService.sendEmailConfirmation(order);

    }
}
