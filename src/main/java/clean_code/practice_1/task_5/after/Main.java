package clean_code.practice_1.task_5.after;

public class Main {
    public static void main(String[] args) {
        PaymentProcessor paymentProcessor1 = new PaymentProcessor();
        PaymentProcessor paymentProcessor2 = new PaymentProcessor();
        PaymentProcessor paymentProcessor3 = new PaymentProcessor();
        CreditCardStrategy creditCardStrategy = new CreditCardStrategy();
        BitcoinStrategy bitcoinStrategy = new BitcoinStrategy();
        PayPalStrategy payPalStrategy = new PayPalStrategy();

        paymentProcessor1.processPayment(creditCardStrategy, 1000.0);
        paymentProcessor1.processPayment(bitcoinStrategy, 2000.0);
        paymentProcessor1.processPayment(payPalStrategy, 3000.0);
    }
}
