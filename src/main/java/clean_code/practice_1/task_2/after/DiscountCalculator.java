package clean_code.practice_1.task_2.after;

public class DiscountCalculator {
    public double calculateDiscount(double price, boolean isLoyalCustomer, boolean isFirstPurchase, boolean hasCoupon) {
        double discountRate = 0.0;

        if (isLoyalCustomer && isFirstPurchase) {
            discountRate = 0.10;
        } else if (isLoyalCustomer)  {
            discountRate = 0.05;
        } else if (hasCoupon) {
            discountRate = 0.07;
        } else {
            discountRate = 0.02;
        }

        return price - (price * discountRate);
    }
}
