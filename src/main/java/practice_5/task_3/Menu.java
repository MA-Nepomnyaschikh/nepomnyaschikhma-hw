package main.java.practice_5.task_3;

public class Menu {

    private Dish dish;

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public void printMenu() {
        dish.printInfo();
    }
}
