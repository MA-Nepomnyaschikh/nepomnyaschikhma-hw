package clean_code.practice_1.task_5.after;

public class QRCodeStrategy implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Оплата по QR-коду на сумму " + amount);
    }
}
