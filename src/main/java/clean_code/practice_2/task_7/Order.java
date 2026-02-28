package clean_code.practice_2.task_7;

import java.util.ArrayList;
import java.util.List;

public class Order {
    List<String> products;
    double discount;
    String paymentMethod;

    private Order(Builder builder) {
        this.products = builder.products;
        this.discount = builder.discount;
        this.paymentMethod = builder.paymentMethod;
    }

    @Override
    public String toString() {
        return "Order: " +
                "Product list: " + products +
                ", Discount: " + discount +
                ", Payment method: " + paymentMethod;
    }

    static class Builder {
        List<String> products = new ArrayList<>();
        double discount;
        String paymentMethod;

        public Builder addProducts(String product) {
            this.products.add(product);
            return this;
        }

        public Builder setDiscount(double discount) {
            this.discount = discount;
            return this;
        }

        public Builder setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
