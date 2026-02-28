package clean_code.practice_2.task_5;

public class ModernFurnitureFactory extends FurnitureFactory {
    @Override
    Chair createChair() {
        return new ModernChair();
    }

    @Override
    Chest createChest() {
        return new ModernChest();
    }

    @Override
    Table createTable() {
        return new ModernTable();
    }
}
