package clean_code.practice_1.task_5.after;

public class BitcoinStrategy implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Оплата Bitcoin на сумму " + amount);
    }
}
