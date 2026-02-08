package clean_code.practice_1.task_4.after;

public class Order {

    private final int orderNumber;
    private int countProducts;
    private double totalPrice;

    public Order(int orderNumber, int countProducts, double totalPrice) {
        this.orderNumber = orderNumber;
        this.countProducts = countProducts;
        this.totalPrice = totalPrice;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getCountProducts() {
        return countProducts;
    }

    public void setCountProducts(int countProducts) {
        this.countProducts = countProducts;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
