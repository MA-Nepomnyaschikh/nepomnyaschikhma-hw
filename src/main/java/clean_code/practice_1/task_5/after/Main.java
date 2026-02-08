package clean_code.practice_1.task_5.after;

public class Main {
    public static void main(String[] args) {
        PaymentProcessor paymentProcessor = new PaymentProcessor();
        CreditCardStrategy creditCardStrategy = new CreditCardStrategy();
        BitcoinStrategy bitcoinStrategy = new BitcoinStrategy();
        PayPalStrategy payPalStrategy = new PayPalStrategy();
        QRCodeStrategy qRCodeStrategy = new QRCodeStrategy();

        paymentProcessor.processPayment(creditCardStrategy, 1000.0);
        paymentProcessor.processPayment(bitcoinStrategy, 2000.0);
        paymentProcessor.processPayment(payPalStrategy, 3000.0);
        paymentProcessor.processPayment(qRCodeStrategy, 4000.0);
    }
}
