package clean_code.practice_1.task_4.before;

public class Order {
    public void processOrder() {
        System.out.println("Обрабатываем заказ...");
    }
    public void sendEmailConfirmation() {
        System.out.println("Отправляем письмо клиенту...");
    }
    public void generateInvoice() {
        System.out.println("Генерируем счет...");
    }
}
