package clean_code.practice_1.task_2.after;

public class Main {
    public static void main(String[] args) {
        DiscountCalculator discountCalculator = new DiscountCalculator();
        System.out.println(discountCalculator.calculateDiscount(1000, true, true, true));
        System.out.println(discountCalculator.calculateDiscount(1000, true, false, true));
        System.out.println(discountCalculator.calculateDiscount(1000, false, false, true));
        System.out.println(discountCalculator.calculateDiscount(1000, false, false, false));
    }
}
