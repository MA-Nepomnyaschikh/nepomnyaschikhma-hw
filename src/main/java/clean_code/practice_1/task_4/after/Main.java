package clean_code.practice_1.task_4.after;

public class Main {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        orderService.processOrder();
        InvoiceService invoiceService = new InvoiceService();
        invoiceService.generateInvoice();
        NotificationService notificationService = new NotificationService();
        notificationService.sendEmailConfirmation();

    }
}
