package express_java.practice_5.task_3;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        Dish hotDish = new HotDish(65.0);
        Dish drink = new Drink(300);
        menu.setDish(hotDish);
        menu.printMenu();
    }
}
