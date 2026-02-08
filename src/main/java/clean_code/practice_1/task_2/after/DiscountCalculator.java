package clean_code.practice_1.task_2.after;

public class DiscountCalculator {
    private static final double LOYAL_FIRST_PURCHASE_DISCOUNT = 0.10;
    private static final double LOYAL_CUSTOMER_DISCOUNT = 0.05;
    private static final double COUPON_DISCOUNT = 0.07;
    private static final double DEFAULT_DISCOUNT = 0.02;

    public double calculateDiscount(double price, boolean isLoyalCustomer, boolean isFirstPurchase, boolean hasCoupon) {
        double discountRate = 0.0;

        if (isLoyalCustomer && isFirstPurchase) {
            discountRate = LOYAL_FIRST_PURCHASE_DISCOUNT;
        } else if (isLoyalCustomer)  {
            discountRate = LOYAL_CUSTOMER_DISCOUNT;
        } else if (hasCoupon) {
            discountRate = COUPON_DISCOUNT;
        } else {
            discountRate = DEFAULT_DISCOUNT;
        }

        return price - (price * discountRate);
    }
}
