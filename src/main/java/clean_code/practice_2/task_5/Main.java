package clean_code.practice_2.task_5;

public class Main {
    public static void main(String[] args) {
        FurnitureFactory factory = new ClassicFurnitureFactory();

        factory.createTable().placeDishes();
        factory.createChair().sitOn();
        factory.createChest().putClothes();

        System.out.println();

        factory = new ModernFurnitureFactory();

        factory.createTable().placeDishes();
        factory.createChair().sitOn();
        factory.createChest().putClothes();
    }
}
