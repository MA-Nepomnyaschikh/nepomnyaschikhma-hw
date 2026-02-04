package clean_code.practice_1.task_5.after;

public class PaymentProcessor {
    public void processPayment(PaymentStrategy paymentStrategy, double amount) {
        paymentStrategy.pay(amount);
    }
}
