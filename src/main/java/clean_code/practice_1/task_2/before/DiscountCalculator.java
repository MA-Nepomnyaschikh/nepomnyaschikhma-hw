package clean_code.practice_1.task_2.before;

public class DiscountCalculator {
    public double calculateDiscount(double price, boolean isLoyalCustomer, boolean isFirstPurchase, boolean hasCoupon) {
        double discount = 0.0;

        if (isLoyalCustomer) {
            if (isFirstPurchase) {
                discount = price * 0.10;
            } else {
                discount = price * 0.05;
            }
        } else {
            if (hasCoupon) {
                discount = price * 0.07;
            } else {
                discount = price * 0.02;
            }
        }
        return price - discount;
    }
}
