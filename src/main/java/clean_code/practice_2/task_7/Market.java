package clean_code.practice_2.task_7;

public class Market {
    public static void main(String[] args) {
        Order order = new Order.Builder()
                .addProducts("Banana")
                .setDiscount(10.0)
                .setPaymentMethod("creditCard")
                .build();

        System.out.println(order);
    }
}
